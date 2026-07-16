package com.group.artifName.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketSolvedMessage {

    @NotBlank(message = "El mensaje es obligatorio")
    private String message; // Aquí mandarán "EN_PROCESO" o "RESUELTO"
}