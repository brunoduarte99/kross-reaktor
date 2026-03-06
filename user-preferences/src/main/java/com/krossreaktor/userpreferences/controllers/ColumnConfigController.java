package com.krossreaktor.userpreferences.controllers;

import com.krossreaktor.userpreferences.dtos.columnConfig.ColumnConfigDto;
import com.krossreaktor.userpreferences.dtos.columnConfig.UpdateColumnDto;
import com.krossreaktor.userpreferences.models.Column;
import com.krossreaktor.userpreferences.services.ColumnConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-preferences/column-config")
@Tag(name = "Column Config", description = "Endpoints for managing user column configurations per table")
public class ColumnConfigController {

    private final ColumnConfigService columnConfigService;

    public ColumnConfigController(ColumnConfigService columnConfigService) {
        this.columnConfigService = columnConfigService;
    }

    @Operation(summary = "Get column configuration for a specific table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Column.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{userId}/{tableId}")
    public ResponseEntity<List<Column>> getColumnConfig(
            @PathVariable UUID userId,
            @PathVariable String tableId
    ) {
        return ResponseEntity.ok(columnConfigService.getTableConfiguration(userId, tableId));
    }

    @Operation(summary = "Get all column configurations for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ColumnConfigDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ColumnConfigDto> getAllColumnConfigs(@PathVariable UUID userId) {
        return ResponseEntity.ok(columnConfigService.getOrCreateDefault(userId));
    }

    @Operation(summary = "Update column configuration for a specific table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Column.class)))),
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
    @PatchMapping("/{userId}/{tableId}")
    public ResponseEntity<List<Column>> updateColumnConfig(
            @PathVariable UUID userId,
            @PathVariable String tableId,
            @RequestBody @Valid List<@Valid UpdateColumnDto> columns
    ) {
        return ResponseEntity.ok(columnConfigService.updateTableConfiguration(userId, tableId, columns));
    }
}
