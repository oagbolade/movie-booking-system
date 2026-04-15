package com.platform.catalogue.migration;

import io.mongock.api.annotations.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;

@ChangeUnit(id = "seed-movies", order = "002", author = "system")
public class V2__seed_movies {

    @Execution
    public void execute(MongoDatabase db) {
        db.getCollection("movies").insertOne(
                new Document("title", "Inception")
                        .append("description", "Sci-fi thriller")
                        .append("genre", "SCI-FI")
                        .append("duration", 148)
                        .append("language", "EN")
                        .append("createdAt", LocalDateTime.now().toString())
        );
    }

    @RollbackExecution
    public void rollback(MongoDatabase db) {
        db.getCollection("movies").deleteMany(new Document("title", "Inception"));
    }
}