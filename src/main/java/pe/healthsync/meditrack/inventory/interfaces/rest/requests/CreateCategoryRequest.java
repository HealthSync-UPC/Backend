package pe.healthsync.meditrack.inventory.interfaces.rest.requests;

import pe.healthsync.meditrack.inventory.domain.model.commands.CreateCategoryCommand;

public record CreateCategoryRequest(
        String name,
        String description) {

    public CreateCategoryCommand toCommand(Long adminId) {
        return new CreateCategoryCommand(adminId, name, description);
    }
}
