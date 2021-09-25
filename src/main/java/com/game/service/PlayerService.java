package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.*;
import com.game.repository.PlayerCriteriaRep;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerCriteriaRep playerCriteriaRep;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerCriteriaRep playerCriteriaRep) {
        this.playerRepository = playerRepository;
        this.playerCriteriaRep = playerCriteriaRep;

    }

    public Player findById(Long id) {
        return playerRepository.findById(id).get();
    }

    public List<Player> findPlayers(String name, String title, Race race, Profession profession,
                                    Long fromDate, Long toDate,
                                    Boolean banned, Integer minExperience, Integer maxExperience,
                                    Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize,
                                    PlayerOrder order) {
        return playerCriteriaRep.findPlayers(name, title, race, profession, fromDate, toDate, banned, minExperience,
                maxExperience, minLevel, maxLevel, pageNumber, pageSize, order);
    }

    public Long findPlayerCount(String name, String title, Race race, Profession profession,
                               Long fromDate, Long toDate,
                               Boolean banned, Integer minExperience, Integer maxExperience,
                               Integer minLevel, Integer maxLevel) {
        return playerCriteriaRep.findPlayerCount(name, title, race, profession, fromDate, toDate, banned, minExperience,
                maxExperience, minLevel, maxLevel);

    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deleteById(Long id){
        playerRepository.deleteById(id);
    }
}