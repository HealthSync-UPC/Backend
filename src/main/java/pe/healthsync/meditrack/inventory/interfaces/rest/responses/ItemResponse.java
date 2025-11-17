package pe.healthsync.meditrack.inventory.interfaces.rest.responses;

import java.time.LocalDate;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;

public record ItemResponse(
        Long id,
        String categoryName,
        String name,
        String code,
        String description,
        int quantity,
        String unit,
        boolean active,
        String location,
        LocalDate expirationDate) {

    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getCategory().getName(),
                item.getName(),
                item.getCode(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit(),
                item.isActive(),
                item.getZone().getName(),
                item.getExpirationDate());
    }
}
