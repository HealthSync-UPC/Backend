package pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByAdminId(Long adminId);
}
