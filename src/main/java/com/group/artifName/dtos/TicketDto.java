package com.group.artifName.dtos;

import com.group.artifName.entities.Area;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketDto {

    @NotBlank(message = "El título del ticket es obligatorio")
    private String title;

    @NotBlank(message = "La descripción del ticket es obligatoria")
    private String description;

    @NotNull(message = "El área del ticket es obligatoria")
    private Area area;
}