package org.example.futtracker.repositories;

import org.example.futtracker.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByName(String fcDynamo);
}
