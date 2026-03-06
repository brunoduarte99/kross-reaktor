package com.krossreaktor.userpreferences.dtos.tableDefinition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDefinitionRequestDto {
    @NotBlank
    private String tableId;
    @NotEmpty
    private List<String> columnKeys;
}