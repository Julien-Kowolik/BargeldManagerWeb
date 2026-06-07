package de.julienkowolik.bargeldmanager.repository;

import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotRepository
        extends JpaRepository<DepotEntity, Long> {
}