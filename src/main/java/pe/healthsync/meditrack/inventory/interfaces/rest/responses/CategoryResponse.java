package pe.healthsync.meditrack.inventory.interfaces.rest.responses;

import java.util.List;

import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;

public record CategoryResponse(
                Long id,
                String name,
                String description,
                List<ItemResponse> items) {

        public static CategoryResponse fromEntity(Category category) {
                var items = category.getItems().stream()
                                .map(ItemResponse::fromEntity)
                                .toList();

                return new CategoryResponse(
                                category.getId(),
                                category.getName(),
                                category.getDescription(),
                                items);
        }
}
