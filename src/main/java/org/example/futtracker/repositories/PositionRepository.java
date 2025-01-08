package org.example.futtracker.repositories;

import org.example.futtracker.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findByName(String forward);
}
