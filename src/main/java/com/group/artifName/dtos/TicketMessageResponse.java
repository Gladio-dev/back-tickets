package com.group.artifName.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketMessageResponse {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private Long senderId;

    private String senderName;
}