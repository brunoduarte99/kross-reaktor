package com.krossreaktor.userpreferences.dtos.filterGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterGroupRequestDto {
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotEmpty
    private Map<String, Object> filters;
}
