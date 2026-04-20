package org.example.service;

import org.example.dto.ItemDto;
import org.example.model.Item;
import org.example.repo.ItemRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


@Service
public class ItemService {
 private static final Logger logger= LoggerFactory.getLogger(ItemService.class);
    @Autowired
    ItemRepository itemRepository;
    public boolean addItems(ItemDto itemDto) {

        // ✅ validation
        if (itemDto == null || itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
            logger.warn("Invalid item data received");
            return false;
        }

        try {
            // DTO → Entity (Java 8 style optional usage not required here but clean mapping)
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setCost(itemDto.getCost());

            itemRepository.save(item);

            logger.info("Item saved successfully: {}", item.getName());

            return true;

        } catch (Exception e) {
            logger.error("Error while saving item: {}", itemDto.getName(), e);
            return false;
        }
    }

    public Item getItemById(String id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }
}
