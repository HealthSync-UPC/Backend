package pe.healthsync.meditrack.accesscontrol.infrastructure.persistence.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findAllByAdminId(Long adminId);
}
