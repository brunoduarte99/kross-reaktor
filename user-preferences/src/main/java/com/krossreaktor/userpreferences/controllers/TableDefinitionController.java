package com.krossreaktor.userpreferences.controllers;

import com.krossreaktor.userpreferences.dtos.tableDefinition.TableDefinitionRequestDto;
import com.krossreaktor.userpreferences.dtos.tableDefinition.TableDefinitionResponseDto;
import com.krossreaktor.userpreferences.services.TableDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-preferences/tables")
@Tag(name = "Table Definitions", description = "Endpoints for managing table definitions and their column keys")
public class TableDefinitionController {

    private final TableDefinitionService tableDefinitionService;

    public TableDefinitionController(TableDefinitionService tableDefinitionService) {
        this.tableDefinitionService = tableDefinitionService;
    }

    @Operation(summary = "Get all table definitions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<TableDefinitionResponseDto>> getAll() {
        return ResponseEntity.ok(tableDefinitionService.getAll());
    }

    @Operation(summary = "Get a table definition by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{tableId}")
    public ResponseEntity<TableDefinitionResponseDto> getById(@PathVariable String tableId) {
        return ResponseEntity.ok(tableDefinitionService.getById(tableId));
    }

    @Operation(summary = "Create a new table definition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<TableDefinitionResponseDto> create(@RequestBody @Valid TableDefinitionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableDefinitionService.create(request));
    }

    @Operation(summary = "Update a table definition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{tableId}")
    public ResponseEntity<TableDefinitionResponseDto> update(
            @PathVariable String tableId,
            @RequestBody @Valid TableDefinitionRequestDto request
    ) {
        return ResponseEntity.ok(tableDefinitionService.update(tableId, request.getColumnKeys()));
    }

    @Operation(summary = "Delete a table definition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> delete(@PathVariable String tableId) {
        tableDefinitionService.delete(tableId);
        return ResponseEntity.noContent().build();
    }
}