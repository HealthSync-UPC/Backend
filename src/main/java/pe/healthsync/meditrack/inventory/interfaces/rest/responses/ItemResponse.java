package pe.healthsync.meditrack.inventory.interfaces.rest.responses;

import pe.healthsync.meditrack.inventory.domain.model.entities.Item;

public record ItemResponse(
        Long id,
        String name,
        String code,
        String description,
        int quantity,
        String unit,
        boolean active,
        String location) {

    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getCode(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit(),
                item.isActive(),
                item.getLocation());
    }
}
