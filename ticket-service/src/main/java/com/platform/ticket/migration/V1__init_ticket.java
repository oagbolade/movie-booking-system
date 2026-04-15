package com.platform.ticket.migration;

import io.mongock.api.annotations.*;
import com.mongodb.client.MongoDatabase;

@ChangeUnit(id = "init-ticket", order = "001", author = "system")
public class V1__init_ticket {

    @Execution
    public void execute(MongoDatabase db) {
        db.createCollection("tickets");
    }

    @RollbackExecution
    public void rollback(MongoDatabase db) {
        db.getCollection("tickets").drop();
    }
}