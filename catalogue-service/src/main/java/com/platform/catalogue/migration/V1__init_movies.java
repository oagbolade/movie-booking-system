package com.platform.catalogue.migration;

import io.mongock.api.annotations.*;
import com.mongodb.client.MongoDatabase;

@ChangeUnit(id = "init-movies", order = "001", author = "system")
public class V1__init_movies {

    @Execution
    public void execute(MongoDatabase db) {
        db.createCollection("movies");
    }

    @RollbackExecution
    public void rollback(MongoDatabase db) {
        db.getCollection("movies").drop();
    }
}