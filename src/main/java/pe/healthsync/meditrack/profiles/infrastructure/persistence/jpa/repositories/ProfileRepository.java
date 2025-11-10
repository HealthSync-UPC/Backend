package pe.healthsync.meditrack.profiles.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);

    List<Profile> findAllByAdminId(Long adminId);
}
