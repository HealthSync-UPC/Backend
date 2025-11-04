package pe.healthsync.meditrack.inventory.domain.services;

import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateCategoryCommand;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateItemCommand;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;

public interface InventoryCommandService {
    Category handle(CreateCategoryCommand command);

    Item handle(CreateItemCommand command);
}
