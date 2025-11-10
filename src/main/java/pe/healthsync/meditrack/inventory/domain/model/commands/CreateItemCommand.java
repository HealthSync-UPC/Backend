package pe.healthsync.meditrack.inventory.domain.model.commands;

public record CreateItemCommand(
                Long categoryId,
                String name,
                String code,
                String description,
                int quantity,
                String unit,
                String location) {

}
