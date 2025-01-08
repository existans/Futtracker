package org.example.futtracker.services;

import org.example.futtracker.models.Team;
import org.example.futtracker.repositories.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    public Optional<Team> findTeamById(int id) {
        return teamRepository.findById(id);
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    public void updateTeam(Team updatedTeam) {
        Team existingTeam = teamRepository.findById(updatedTeam.getId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        existingTeam.setName(updatedTeam.getName());
        existingTeam.setLocation(updatedTeam.getLocation());

        teamRepository.save(existingTeam);
    }

    public void deleteTeam(int id) {
        teamRepository.deleteById(id);
    }
}
