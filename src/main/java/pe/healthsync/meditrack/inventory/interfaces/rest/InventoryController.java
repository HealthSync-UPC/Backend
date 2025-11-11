package pe.healthsync.meditrack.inventory.interfaces.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllCategoriesByAdminIdQuery;
import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllItemsByAdminIdQuery;
import pe.healthsync.meditrack.inventory.domain.services.InventoryCommandService;
import pe.healthsync.meditrack.inventory.domain.services.InventoryQueryService;
import pe.healthsync.meditrack.inventory.interfaces.rest.requests.CreateCategoryRequest;
import pe.healthsync.meditrack.inventory.interfaces.rest.requests.CreateItemRequest;
import pe.healthsync.meditrack.inventory.interfaces.rest.responses.CategoryResponse;
import pe.healthsync.meditrack.inventory.interfaces.rest.responses.ItemResponse;
import pe.healthsync.meditrack.shared.infrastructure.security.AuthenticatedUserProvider;

@RestController
@RequestMapping(value = "/api/v1/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory", description = "Inventory Endpoints")
public class InventoryController {

        @Autowired
        private InventoryCommandService inventoryCommandService;

        @Autowired
        private InventoryQueryService inventoryQueryService;

        @Autowired
        private AuthenticatedUserProvider authenticatedUserProvider;

        @Operation(summary = "Get all categories for the current user", description = "Returns a list of all categories associated with the current user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
        })
        @GetMapping("/categories")
        public ResponseEntity<List<CategoryResponse>> getCategories() {
                var admin = authenticatedUserProvider.getCurrentUser();
                var query = new GetAllCategoriesByAdminIdQuery(admin.getId());
                var categories = inventoryQueryService.handle(query);

                var responses = categories.stream()
                                .map(CategoryResponse::fromEntity)
                                .toList();

                return ResponseEntity.ok(responses);
        }

        @Operation(summary = "Create a new category", description = "Creates a new category for the current user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
        })
        @PostMapping("/categories")
        public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
                var admin = authenticatedUserProvider.getCurrentUser();
                var command = request.toCommand(admin.getId());

                var category = inventoryCommandService.handle(command);

                var response = CategoryResponse.fromEntity(category);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Get all items for the current user", description = "Returns a list of all items associated with the current user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of items retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
        })
        @GetMapping("/items")
        public ResponseEntity<List<ItemResponse>> getItems() {
                var admin = authenticatedUserProvider.getCurrentUser();
                var query = new GetAllItemsByAdminIdQuery(admin.getId());
                var items = inventoryQueryService.handle(query);

                var responses = items.stream()
                                .map(ItemResponse::fromEntity)
                                .toList();

                return ResponseEntity.ok(responses);
        }

        @Operation(summary = "Create a new item", description = "Creates a new item under a category.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Item created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
        })
        @PostMapping("/items")
        public ResponseEntity<ItemResponse> createItem(@RequestBody CreateItemRequest request) {
                var command = request.toCommand();

                var item = inventoryCommandService.handle(command);

                var response = ItemResponse.fromEntity(item);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
}
