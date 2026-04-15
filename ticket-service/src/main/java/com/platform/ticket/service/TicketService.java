package com.platform.ticket.service;

import com.platform.ticket.dto.*;
import com.platform.ticket.model.Ticket;
import com.platform.ticket.repository.TicketRepository;
import com.platform.ticket.event.producer.TicketEventProducer;
import com.platform.common.event.NotificationRequestedEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;
    private final TicketEventProducer producer;

    public TicketResponse bookTicket(TicketRequest request) {

        // ✅ IDEMPOTENCY CHECK
        return repository.findByIdempotencyKey(request.getIdempotencyKey())
                .map(this::toResponse)
                .orElseGet(() -> createNewTicket(request));
    }

    private TicketResponse createNewTicket(TicketRequest request) {

        Ticket ticket = Ticket.builder()
                .userId(request.getUserId())
                .movieId(request.getMovieId())
                .idempotencyKey(request.getIdempotencyKey())
                .createdAt(LocalDateTime.now())
                .build();

        Ticket saved = repository.save(ticket);

        // 🔥 Publish event
        NotificationRequestedEvent event = NotificationRequestedEvent.builder()
                .userId(saved.getUserId())
                .type("EMAIL")
                .message("🎟️ Your ticket has been booked successfully!")
                .build();

        producer.sendTicketNotification(event);

        return toResponse(saved);
    }

    private TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .movieId(ticket.getMovieId())
                .build();
    }
}