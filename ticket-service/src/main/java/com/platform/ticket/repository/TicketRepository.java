package com.platform.ticket.repository;

import com.platform.ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Optional<Ticket> findByIdempotencyKey(String idempotencyKey);
}