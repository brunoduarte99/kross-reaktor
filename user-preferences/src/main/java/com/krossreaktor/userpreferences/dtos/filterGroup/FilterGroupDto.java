package com.krossreaktor.userpreferences.dtos.filterGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterGroupDto {
    private String id;
    private String name;
    private Map<String, Object> filters;
}
