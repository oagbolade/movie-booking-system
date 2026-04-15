package com.platform.auth.migration;

import com.platform.auth.model.User;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;

@ChangeUnit(id = "seed-admin", order = "002", author = "system")
public class V2__seed_admin {

    @Execution
    public void execute(MongoTemplate mongoTemplate) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User admin = User.builder()
                .email("admin@platform.com")
                .password(encoder.encode("admin123"))
                .roles(List.of("ADMIN"))
                .isActive(true)
                .createdAt(Instant.now())
                .build();

        mongoTemplate.save(admin);
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.remove(new Query(Criteria.where("email").is("admin@platform.com")), User.class);
    }
}