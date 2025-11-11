package pe.healthsync.meditrack.inventory.application.queryservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;
import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllCategoriesByAdminIdQuery;
import pe.healthsync.meditrack.inventory.domain.model.queries.GetAllItemsByAdminIdQuery;
import pe.healthsync.meditrack.inventory.domain.services.InventoryQueryService;
import pe.healthsync.meditrack.inventory.infrastructure.persistence.jpa.respositories.CategoryRepository;

@Service
public class InventoryQueryServiceImpl implements InventoryQueryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> handle(GetAllCategoriesByAdminIdQuery query) {
        return categoryRepository.findByAdminId(query.adminId());
    }

    @Override
    public List<Item> handle(GetAllItemsByAdminIdQuery query) {
        var categories = this.categoryRepository.findByAdminId(query.adminId());
        var items = categories.stream()
                .flatMap(category -> category.getItems().stream())
                .toList();

        return items;
    }
}
