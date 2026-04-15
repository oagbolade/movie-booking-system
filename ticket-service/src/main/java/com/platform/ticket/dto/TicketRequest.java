package com.platform.ticket.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private String userId;
    private String movieId;
    private String idempotencyKey;
}