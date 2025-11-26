package pe.healthsync.meditrack.inventory.interfaces.rest.requests;

import java.time.LocalDate;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateItemCommand;

public record CreateItemRequest(
        Long categoryId,
        String name,
        String code,
        String description,
        int quantity,
        String unit,
        LocalDate expirationDate) {

    public CreateItemCommand toCommand() {
        return new CreateItemCommand(
                categoryId,
                name,
                code,
                description,
                quantity,
                unit,
                expirationDate);
    }
}
