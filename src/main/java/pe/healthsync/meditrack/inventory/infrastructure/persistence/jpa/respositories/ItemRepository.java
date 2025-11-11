package pe.healthsync.meditrack.inventory.infrastructure.persistence.jpa.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.healthsync.meditrack.inventory.domain.model.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
