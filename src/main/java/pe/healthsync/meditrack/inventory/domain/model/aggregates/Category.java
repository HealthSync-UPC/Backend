package pe.healthsync.meditrack.inventory.domain.model.aggregates;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateCategoryCommand;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateItemCommand;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Category extends AuditableAbstractAggregateRoot<Category> {
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public Category(CreateCategoryCommand command, User admin) {
        this.admin = admin;
        this.name = command.name();
        this.description = command.description();
    }

    public void addItem(CreateItemCommand command) {
        var item = new Item(command);
        items.add(item);
        item.setCategory(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

}
