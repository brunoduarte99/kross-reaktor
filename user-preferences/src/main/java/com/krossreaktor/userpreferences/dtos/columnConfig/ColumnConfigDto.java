package com.krossreaktor.userpreferences.dtos.columnConfig;

import com.krossreaktor.userpreferences.models.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnConfigDto {
    private UUID userId;
    private Map<String, List<Column>> configurations;
}