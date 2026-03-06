package com.krossreaktor.userpreferences.repositories;

import com.krossreaktor.userpreferences.models.TableDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableDefinitionRepository extends MongoRepository<TableDefinition, String> {
}
