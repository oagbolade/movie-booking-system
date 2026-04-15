package com.platform.auth.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "init-users", order = "001", author = "system")
public class V1__init_users {

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("users");
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("users");
    }
}