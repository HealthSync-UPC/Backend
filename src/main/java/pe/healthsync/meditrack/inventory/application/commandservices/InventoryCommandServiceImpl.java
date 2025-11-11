package pe.healthsync.meditrack.inventory.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateCategoryCommand;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateItemCommand;
import pe.healthsync.meditrack.inventory.domain.model.commands.DeleteCategoryCommand;
import pe.healthsync.meditrack.inventory.domain.model.commands.DeleteItemCommand;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;
import pe.healthsync.meditrack.inventory.domain.services.InventoryCommandService;
import pe.healthsync.meditrack.inventory.infrastructure.persistence.jpa.respositories.CategoryRepository;

@Service
public class InventoryCommandServiceImpl implements InventoryCommandService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Category handle(CreateCategoryCommand command) {
        var admin = userRepository.findById(command.adminId())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        var category = new Category(command, admin);

        return categoryRepository.save(category);
    }

    @Override
    public Item handle(CreateItemCommand command) {
        var category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.addItem(command);

        categoryRepository.save(category);

        var item = category.getItems().get(category.getItems().size() - 1);

        return item;
    }

    @Override
    public void handle(DeleteCategoryCommand command) {
        var category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + command.categoryId()));

        if (!category.getAdmin().getId().equals(command.adminId())) {
            throw new IllegalArgumentException("User not authorized to delete this category");
        }

        categoryRepository.delete(category);
    }

    @Override
    public void handle(DeleteItemCommand command) {
        var category = categoryRepository.findByItemsId(command.itemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + command.itemId()));

        if (!category.getAdmin().getId().equals(command.adminId())) {
            throw new IllegalArgumentException("User not authorized to delete this item");
        }

        category.removeItem(command.itemId());

        categoryRepository.save(category);
    }

}
