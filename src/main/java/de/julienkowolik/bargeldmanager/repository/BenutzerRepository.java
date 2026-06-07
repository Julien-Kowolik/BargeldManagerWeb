package de.julienkowolik.bargeldmanager.repository;
import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenutzerRepository
        extends JpaRepository<BenutzerEntity, Long> {
    Optional<BenutzerEntity> findByName(String name);
}
