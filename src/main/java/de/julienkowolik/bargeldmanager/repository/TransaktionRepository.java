package de.julienkowolik.bargeldmanager.repository;

import de.julienkowolik.bargeldmanager.entity.TransaktionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionRepository
        extends JpaRepository<TransaktionEntity, Long> {
}