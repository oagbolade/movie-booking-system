package com.platform.ticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {
    private String id;
    private String userId;
    private String movieId;
}