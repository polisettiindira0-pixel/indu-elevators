package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ItemDto;
import org.example.model.Item;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/items")
public class InduElevatorsController {

    @Autowired
    private ItemService itemService;

    private static final Logger logger =
            LoggerFactory.getLogger(InduElevatorsController.class);

    // ✅ Save item
    @PostMapping
    public String addItems(@RequestBody ItemDto itemDto) {
        logger.info("Saving item...");

        boolean result = itemService.addItems(itemDto);

        return result ? "Data saved" : "Not saved";
    }

    // ✅ Download PDF
    @GetMapping("/download-pdf/{id}")
    public void downloadPdf(@PathVariable String id,
                            HttpServletResponse response) {

        try {
            logger.info("Download PDF request for id: {}", id);

            Item item = itemService.getItemById(id);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            if (item != null) {
                document.add(new Paragraph("Hello PDF"));
                document.add(new Paragraph("Item Name: " + item.getName()));
                document.add(new Paragraph("Price: " + item.getCost()));
            } else {
                document.add(new Paragraph("Item not found"));
            }

            document.close();

            // ✅ Send PDF response
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=item_" + id + ".pdf");

            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();

            logger.info("PDF generated successfully for id: {}", id);

        } catch (Exception e) {
            logger.error("Error generating PDF for id: {}", id, e);

            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error generating PDF");
            } catch (Exception ex) {
                logger.error("Failed to send error response", ex);
            }
        }
    }
}