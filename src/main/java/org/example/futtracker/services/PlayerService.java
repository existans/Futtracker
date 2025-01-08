package org.example.futtracker.services;

import org.example.futtracker.models.Player;
import org.example.futtracker.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    public Optional<Player> findPlayerById(int id) {
        return playerRepository.findById(id);
    }

    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    public void updatePlayer(Player updatedPlayer) {
        Player existingPlayer = playerRepository.findById(updatedPlayer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        existingPlayer.setName(updatedPlayer.getName());
        existingPlayer.setAge(updatedPlayer.getAge());
        existingPlayer.setNationality(updatedPlayer.getNationality());
        existingPlayer.setTeam(updatedPlayer.getTeam());
        existingPlayer.setPosition(updatedPlayer.getPosition());

        playerRepository.save(existingPlayer);
    }

    public void deletePlayer(int id) {
        playerRepository.deleteById(id);
    }
}
