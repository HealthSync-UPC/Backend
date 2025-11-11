package pe.healthsync.meditrack.inventory.infrastructure.persistence.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.healthsync.meditrack.inventory.domain.model.aggregates.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByAdminId(Long adminId);
}
