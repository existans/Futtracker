package org.example.futtracker.services;

import org.example.futtracker.models.Team;
import org.example.futtracker.models.Position;
import org.example.futtracker.models.Player;
import org.example.futtracker.repositories.TeamRepository;
import org.example.futtracker.repositories.PositionRepository;
import org.example.futtracker.repositories.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;
    private final PlayerRepository playerRepository;

    public DataInitializer(TeamRepository teamRepository,
                           PositionRepository positionRepository,
                           PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.positionRepository = positionRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Team team1 = teamRepository.findByName("FC Dynamo").orElseGet(() ->
                teamRepository.save(new Team(null, "FC Dynamo", "Kyiv")));
        Team team2 = teamRepository.findByName("Shakhtar Donetsk").orElseGet(() ->
                teamRepository.save(new Team(null, "Shakhtar Donetsk", "Donetsk")));

        Position position1 = positionRepository.findByName("Forward").orElseGet(() ->
                positionRepository.save(new Position(null, "Forward")));
        Position position2 = positionRepository.findByName("Midfielder").orElseGet(() ->
                positionRepository.save(new Position(null, "Midfielder")));

        if (playerRepository.count() == 0) {
            Player player1 = new Player(null, "Andriy Shevchenko", 38, "Ukraine", team1, position1);
            Player player2 = new Player(null, "Yevhen Konoplyanka", 34, "Ukraine", team2, position2);

            playerRepository.save(player1);
            playerRepository.save(player2);
        }
    }
}
