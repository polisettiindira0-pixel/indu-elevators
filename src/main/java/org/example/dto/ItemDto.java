package org.example.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ItemDto {
    private String name;
    private double cost;
}
