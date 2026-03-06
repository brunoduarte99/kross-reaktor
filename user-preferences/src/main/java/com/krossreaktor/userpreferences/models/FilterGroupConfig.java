package com.krossreaktor.userpreferences.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "filter-groups")
public class FilterGroupConfig {
    @Id
    private UUID userId;
    private Map<String, List<FilterGroup>> tables; // tableId -> groups
}