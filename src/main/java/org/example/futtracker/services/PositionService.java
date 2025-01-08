package org.example.futtracker.services;

import org.example.futtracker.models.Position;
import org.example.futtracker.repositories.PositionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    public Optional<Position> findPositionById(int id) {
        return positionRepository.findById(id);
    }

    public void savePosition(Position position) {
        positionRepository.save(position);
    }

    public void updatePosition(Position updatedPosition) {
        Position existingPosition = positionRepository.findById(updatedPosition.getId())
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));

        existingPosition.setName(updatedPosition.getName());

        positionRepository.save(existingPosition);
    }

    public void deletePosition(int id) {
        positionRepository.deleteById(id);
    }
}
