package pe.healthsync.meditrack.inventory.domain.services;

import java.util.List;

import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;
import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllCategoriesByAdminIdQuery;
import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllItemsByAdminIdQuery;

public interface InventoryQueryService {
    List<Category> handle(GetAllCategoriesByAdminIdQuery query);

    List<Item> handle(GetAllItemsByAdminIdQuery query);
}
