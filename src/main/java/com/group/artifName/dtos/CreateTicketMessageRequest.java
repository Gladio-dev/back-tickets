package com.group.artifName.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTicketMessageRequest {

    @NotBlank
    private String content;

}