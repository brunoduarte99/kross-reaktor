package com.krossreaktor.userpreferences.services;

import com.krossreaktor.userpreferences.dtos.TableDefinitionRequestDto;
import com.krossreaktor.userpreferences.dtos.TableDefinitionResponseDto;
import com.krossreaktor.userpreferences.exceptions.BadRequestException;
import com.krossreaktor.userpreferences.exceptions.NotFoundException;
import com.krossreaktor.userpreferences.models.TableDefinition;
import com.krossreaktor.userpreferences.repositories.TableDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableDefinitionService {

    private final TableDefinitionRepository tableDefinitionRepository;

    public TableDefinitionService(TableDefinitionRepository tableDefinitionRepository) {
        this.tableDefinitionRepository = tableDefinitionRepository;
    }

    public List<TableDefinitionResponseDto> getAll() {
        return tableDefinitionRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public TableDefinitionResponseDto getById(String tableId) {
        return toDto(findById(tableId));
    }

    public TableDefinitionResponseDto create(TableDefinitionRequestDto request) {
        if (tableDefinitionRepository.existsById(request.getTableId())) {
            throw new BadRequestException("Table definition already exists: " + request.getTableId());
        }
        TableDefinition saved = tableDefinitionRepository.save(TableDefinition.builder()
                .id(request.getTableId())
                .columnKeys(request.getColumnKeys())
                .build());
        return toDto(saved);
    }

    public TableDefinitionResponseDto update(String tableId, List<String> columnKeys) {
        TableDefinition existing = findById(tableId);
        existing.setColumnKeys(columnKeys);
        return toDto(tableDefinitionRepository.save(existing));
    }

    public void delete(String tableId) {
        findById(tableId);
        tableDefinitionRepository.deleteById(tableId);
    }

    private TableDefinition findById(String tableId) {
        return tableDefinitionRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException("Table definition not found: " + tableId));
    }

    private TableDefinitionResponseDto toDto(TableDefinition tableDefinition) {
        return TableDefinitionResponseDto.builder()
                .tableId(tableDefinition.getId())
                .columnKeys(tableDefinition.getColumnKeys())
                .build();
    }
}
