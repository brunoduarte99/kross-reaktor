package com.krossreaktor.userpreferences.repositories;

import com.krossreaktor.userpreferences.models.ColumnConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ColumnConfigRepository extends MongoRepository<ColumnConfig, UUID> {
}