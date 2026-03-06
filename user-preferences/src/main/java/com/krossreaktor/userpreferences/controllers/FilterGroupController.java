package com.krossreaktor.userpreferences.controllers;

import com.krossreaktor.userpreferences.dtos.filterGroup.FilterGroupDto;
import com.krossreaktor.userpreferences.dtos.filterGroup.FilterGroupRequestDto;
import com.krossreaktor.userpreferences.services.FilterGroupService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-preferences/filter-groups")
@Tag(name = "Filter Groups", description = "Endpoints for managing user saved filter groups per table")
public class FilterGroupController {

    private final FilterGroupService filterGroupService;

    public FilterGroupController(FilterGroupService filterGroupService) {
        this.filterGroupService = filterGroupService;
    }

    @Operation(summary = "Get all filter groups for a user and table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{userId}/{tableId}")
    public ResponseEntity<List<FilterGroupDto>> getFilterGroups(
            @PathVariable UUID userId,
            @PathVariable String tableId
    ) {
        return ResponseEntity.ok(filterGroupService.getFilterGroups(userId, tableId));
    }

    @Operation(summary = "Create a new filter group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
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
    @PostMapping("/{userId}/{tableId}")
    public ResponseEntity<FilterGroupDto> createFilterGroup(
            @PathVariable UUID userId,
            @PathVariable String tableId,
            @RequestBody @Valid FilterGroupRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(filterGroupService.createFilterGroup(userId, tableId, request));
    }

    @Operation(summary = "Update a filter group")
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
    @PutMapping("/{userId}/{tableId}/{groupId}")
    public ResponseEntity<FilterGroupDto> updateFilterGroup(
            @PathVariable UUID userId,
            @PathVariable String tableId,
            @PathVariable String groupId,
            @RequestBody @Valid FilterGroupRequestDto request
    ) {
        return ResponseEntity.ok(filterGroupService.updateFilterGroup(userId, tableId, groupId, request));
    }

    @Operation(summary = "Delete a filter group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{userId}/{tableId}/{groupId}")
    public ResponseEntity<Void> deleteFilterGroup(
            @PathVariable UUID userId,
            @PathVariable String tableId,
            @PathVariable String groupId
    ) {
        filterGroupService.deleteFilterGroup(userId, tableId, groupId);
        return ResponseEntity.noContent().build();
    }
}
