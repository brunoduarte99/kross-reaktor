package com.krossreaktor.userpreferences.repositories;

import com.krossreaktor.userpreferences.models.FilterGroupConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FilterGroupConfigRepository extends MongoRepository<FilterGroupConfig, UUID> {
}
