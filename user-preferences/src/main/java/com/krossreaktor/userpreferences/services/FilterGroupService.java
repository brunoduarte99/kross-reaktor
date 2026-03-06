package com.krossreaktor.userpreferences.services;

import com.krossreaktor.userpreferences.dtos.filterGroup.FilterGroupDto;
import com.krossreaktor.userpreferences.dtos.filterGroup.FilterGroupRequestDto;
import com.krossreaktor.userpreferences.exceptions.BadRequestException;
import com.krossreaktor.userpreferences.exceptions.NotFoundException;
import com.krossreaktor.userpreferences.models.FilterGroup;
import com.krossreaktor.userpreferences.models.FilterGroupConfig;
import com.krossreaktor.userpreferences.repositories.FilterGroupConfigRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class FilterGroupService {

    private final FilterGroupConfigRepository filterGroupConfigRepository;
    private final TableDefinitionService tableDefinitionService;

    public FilterGroupService(FilterGroupConfigRepository filterGroupConfigRepository, TableDefinitionService tableDefinitionService) {
        this.filterGroupConfigRepository = filterGroupConfigRepository;
        this.tableDefinitionService = tableDefinitionService;
    }

    /**
     * Gets all filter groups for a user and table.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier
     * @return list of filter groups
     */
    public List<FilterGroupDto> getFilterGroups(UUID userId, String tableId) {
        tableDefinitionService.getById(tableId);

        return filterGroupConfigRepository.findById(userId)
                .map(config -> config.getTables().getOrDefault(tableId, List.of()))
                .orElse(List.of())
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Creates a new filter group for a user and table.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier
     * @param request the filter group request
     * @return the created filter group
     */
    public FilterGroupDto createFilterGroup(UUID userId, String tableId, FilterGroupRequestDto request) {
        tableDefinitionService.getById(tableId);

        FilterGroupConfig config = filterGroupConfigRepository.findById(userId)
                .orElseGet(() -> FilterGroupConfig.builder()
                        .userId(userId)
                        .tables(new HashMap<>())
                        .build());

        List<FilterGroup> groups = new ArrayList<>(
                config.getTables().getOrDefault(tableId, new ArrayList<>())
        );

        boolean nameExists = groups.stream().anyMatch(g -> g.getName().equals(request.getName()));
        if (nameExists) {
            throw new BadRequestException("A filter group with name '" + request.getName() + "' already exists for this table");
        }

        FilterGroup newGroup = FilterGroup.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .filters(request.getFilters())
                .build();

        groups.add(newGroup);
        config.getTables().put(tableId, groups);
        filterGroupConfigRepository.save(config);

        return toDto(newGroup);
    }

    /**
     * Updates an existing filter group.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier
     * @param groupId the group identifier
     * @param request the updated filter group
     * @return the updated filter group
     */
    public FilterGroupDto updateFilterGroup(UUID userId, String tableId, String groupId, FilterGroupRequestDto request) {
        tableDefinitionService.getById(tableId);

        FilterGroupConfig config = filterGroupConfigRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No filter groups found for user " + userId));

        List<FilterGroup> groups = new ArrayList<>(
                config.getTables().getOrDefault(tableId, new ArrayList<>())
        );

        FilterGroup existing = groups.stream()
                .filter(g -> g.getId().equals(groupId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Filter group not found: " + groupId));

        boolean nameExists = groups.stream()
                .anyMatch(g -> g.getName().equals(request.getName()) && !g.getId().equals(groupId));
        if (nameExists) {
            throw new BadRequestException("A filter group with name '" + request.getName() + "' already exists for this table");
        }

        existing.setName(request.getName());
        existing.setFilters(request.getFilters());

        config.getTables().put(tableId, groups);
        filterGroupConfigRepository.save(config);

        return toDto(existing);
    }

    /**
     * Deletes a filter group.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier
     * @param groupId the group identifier
     */
    public void deleteFilterGroup(UUID userId, String tableId, String groupId) {
        tableDefinitionService.getById(tableId);

        FilterGroupConfig config = filterGroupConfigRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No filter groups found for user " + userId));

        List<FilterGroup> groups = new ArrayList<>(
                config.getTables().getOrDefault(tableId, new ArrayList<>())
        );

        boolean removed = groups.removeIf(g -> g.getId().equals(groupId));
        if (!removed) {
            throw new NotFoundException("Filter group not found: " + groupId);
        }

        config.getTables().put(tableId, groups);
        filterGroupConfigRepository.save(config);
    }

    private FilterGroupDto toDto(FilterGroup group) {
        return FilterGroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .filters(group.getFilters())
                .build();
    }
}