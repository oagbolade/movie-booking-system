package com.platform.payment.migration;

import io.mongock.api.annotations.*;
import com.mongodb.client.MongoDatabase;

@ChangeUnit(id = "init-payment", order = "001", author = "system")
public class V1__init_payment {

    @Execution
    public void execute(MongoDatabase db) {
        db.createCollection("payments");
    }

    @RollbackExecution
    public void rollback(MongoDatabase db) {
        db.getCollection("payments").drop();
    }
}