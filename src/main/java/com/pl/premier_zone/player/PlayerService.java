package com.pl.premier_zone.player;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component //Spring annotation to mark class as comp, tells Spring to manage w/ Spring Container, Spring creates instance of component and manages it's LC
public class PlayerService { //business logic, sits between controller and repository to interact with DB

    //encapsulation of business logic for managing 'players' leveraging repository for CRUD operations

    private final PlayerRepository playerRepository;

    @Autowired //Inject playerRepo bean into service to enable DB interaction
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    //BUSINESS LOGIC

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByTeam(String team) {
        return playerRepository.findAll().stream()
                .filter(player -> team.equals(player.getTeam()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByName(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByPosition(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPos().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByNation(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getNation().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByTeamAndPosition(String team, String position) {
        return playerRepository.findAll().stream()
                .filter(player -> team.equals(player.getTeam()) && position.equals(player.getPos()))
                .collect(Collectors.toList());
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player player) {
        Optional<Player> existingPlayer = playerRepository.findByName(player.getName());
        if (existingPlayer.isPresent()) {
            Player updatedPlayer = existingPlayer.get();
            updatedPlayer.setName(player.getName());
            playerRepository.save(player);
            return updatedPlayer;
        }
        return null;
    }

    @Transactional //maintains data integrity during deletion
    public void removePlayer(String playerName) {
        playerRepository.deleteByName(playerName);
    }
}
