package com.group.artifName.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTicketDto {

    @NotNull(message = "El nuevo estado es obligatorio")
    private Long id; // Aquí mandarán "EN_PROCESO" o "RESUELTO"
}