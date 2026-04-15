package com.platform.ticket.controller;

import com.platform.ticket.dto.*;
import com.platform.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @PostMapping
    public TicketResponse book(@RequestBody TicketRequest request) {
        return service.bookTicket(request);
    }
}