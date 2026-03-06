package com.krossreaktor.userpreferences.dtos.columnConfig;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateColumnDto {
    @NotBlank
    private String columnKey;
    private boolean visible;
}
