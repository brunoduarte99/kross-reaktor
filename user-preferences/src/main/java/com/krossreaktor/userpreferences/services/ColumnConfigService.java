package com.krossreaktor.userpreferences.services;

import com.krossreaktor.userpreferences.dtos.columnConfig.ColumnConfigDto;
import com.krossreaktor.userpreferences.dtos.columnConfig.UpdateColumnDto;
import com.krossreaktor.userpreferences.dtos.tableDefinition.TableDefinitionResponseDto;
import com.krossreaktor.userpreferences.exceptions.BadRequestException;
import com.krossreaktor.userpreferences.models.Column;
import com.krossreaktor.userpreferences.models.ColumnConfig;
import com.krossreaktor.userpreferences.repositories.ColumnConfigRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ColumnConfigService {

    private final ColumnConfigRepository columnConfigRepository;
    private final TableDefinitionService tableDefinitionService;

    public ColumnConfigService(ColumnConfigRepository columnConfigRepository, TableDefinitionService tableDefinitionService) {
        this.columnConfigRepository = columnConfigRepository;
        this.tableDefinitionService = tableDefinitionService;
    }

    /**
     * Gets the column configuration for a specific table.
     * If no configuration exists for the user, creates and returns the default configuration.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier (e.g. "ops-reaktor:bookings")
     * @return list of columns with their settings
     */
    public List<Column> getTableConfiguration(UUID userId, String tableId) {
        return columnConfigRepository.findById(userId)
                .map(config -> config.getConfigurations().get(tableId))
                .orElseGet(() -> initializeDefaultConfig(userId).getConfigurations().get(tableId));
    }

    /**
     * Gets all column configurations for a user.
     * If no configuration exists, creates and returns the default configuration for all registered tables.
     *
     * @param userId the unique identifier of the user
     * @return ColumnConfigDto containing all table configurations
     */
    public ColumnConfigDto getOrCreateDefault(UUID userId) {
        return toDto(columnConfigRepository.findById(userId)
                .orElseGet(() -> initializeDefaultConfig(userId)));
    }

    /**
     * Updates the column configuration for a specific table.
     * Validates columns against the registered table definition.
     * The array index represents the column position.
     *
     * @param userId  the unique identifier of the user
     * @param tableId the table identifier
     * @param columns the new column configuration (array order = display order)
     * @return the updated list of columns
     */
    public List<Column> updateTableConfiguration(UUID userId, String tableId, List<UpdateColumnDto> columns) {
        validateColumns(tableId, columns);

        ColumnConfig config = columnConfigRepository.findById(userId)
                .orElseGet(() -> initializeDefaultConfig(userId));

        List<Column> updatedColumns = columns.stream()
                .map(dto -> Column.builder()
                        .columnKey(dto.getColumnKey())
                        .visible(dto.isVisible())
                        .build())
                .toList();

        config.getConfigurations().put(tableId, updatedColumns);

        return columnConfigRepository.save(config).getConfigurations().get(tableId);
    }

    /**
     * Initializes default column configurations for all registered table definitions.
     *
     * @param userId the unique identifier of the user
     * @return the saved ColumnConfig with default settings
     */
    public ColumnConfig initializeDefaultConfig(UUID userId) {
        Map<String, List<Column>> configurations = new HashMap<>();

        tableDefinitionService.getAll().forEach(table ->
                configurations.put(table.getTableId(), createDefaultColumns(table.getColumnKeys()))
        );

        return columnConfigRepository.save(ColumnConfig.builder()
                .userId(userId)
                .configurations(configurations)
                .build());
    }

    private List<Column> createDefaultColumns(List<String> columnKeys) {
        return columnKeys.stream()
                .map(key -> Column.builder()
                        .columnKey(key)
                        .visible(true)
                        .build())
                .toList();
    }

    private void validateColumns(String tableId, List<UpdateColumnDto> columns) {
        TableDefinitionResponseDto tableDefinition = tableDefinitionService.getById(tableId);
        Set<String> validKeys = new HashSet<>(tableDefinition.getColumnKeys());

        if (columns.size() != validKeys.size()) {
            throw new BadRequestException("Expected " + validKeys.size() + " columns but received " + columns.size());
        }

        Set<String> receivedKeys = new HashSet<>();
        for (UpdateColumnDto column : columns) {
            if (!receivedKeys.add(column.getColumnKey())) {
                throw new BadRequestException("Duplicate column key: " + column.getColumnKey());
            }
            if (!validKeys.contains(column.getColumnKey())) {
                throw new BadRequestException("Invalid column key: " + column.getColumnKey());
            }
        }
    }

    private ColumnConfigDto toDto(ColumnConfig config) {
        return ColumnConfigDto.builder()
                .userId(config.getUserId())
                .configurations(config.getConfigurations())
                .build();
    }
}
