package pe.healthsync.meditrack.inventory.domain.model.commands;

import java.time.LocalDate;

public record CreateItemCommand(
                Long categoryId,
                String name,
                String code,
                String description,
                int quantity,
                String unit,
                LocalDate expirationDate) {

}
