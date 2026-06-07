package de.julienkowolik.bargeldmanager.repository;

import de.julienkowolik.bargeldmanager.entity.TransaktionsBargeldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionsBargeldRepository
        extends JpaRepository<TransaktionsBargeldEntity, Long> {
}