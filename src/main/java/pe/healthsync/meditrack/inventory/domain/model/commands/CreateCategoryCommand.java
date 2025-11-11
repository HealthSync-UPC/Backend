package pe.healthsync.meditrack.inventory.domain.model.commands;

public record CreateCategoryCommand(
                Long adminId,
                String name,
                String description) {

}
