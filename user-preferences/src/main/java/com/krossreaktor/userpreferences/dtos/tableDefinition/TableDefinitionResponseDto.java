package com.krossreaktor.userpreferences.dtos.tableDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDefinitionResponseDto {
    private String tableId;
    private List<String> columnKeys;
}
