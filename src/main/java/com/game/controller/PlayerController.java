package com.game.controller;

import com.game.entity.*;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/rest/players")
    public ResponseEntity<List<Player>> findAll(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "race", required = false) Race race,
                                                @RequestParam(value = "profession", required = false) Profession profession,
                                                @RequestParam(value = "after", required = false) Long fromDate,
                                                @RequestParam(value = "before", required = false) Long toDate,
                                                @RequestParam(value = "banned", required = false) Boolean banned,
                                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                @RequestParam(value = "order", required = false, defaultValue = "ID")PlayerOrder order,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                                @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber) {

        List<Player> list = playerService.findPlayers(name, title, race, profession, fromDate, toDate,
                banned, minExperience, maxExperience, minLevel, maxLevel, pageNumber, pageSize, order);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/rest/players/{id}")
    public ResponseEntity<Player> findPlayer(@PathVariable("id") String id) {
        Long ids;
        try {
            ids = Long.parseLong(id);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ids <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player;
        try {
             player = playerService.findById(ids);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/rest/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        ResponseEntity<Player> result = null;
        try {
            Player player1 = playerService.savePlayer(player);
            if (player == null
                    || player.getExperience() > 10000000
                    || player.getName().isEmpty()) {
                result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                Calendar date = Calendar.getInstance();
                if (player.getBirthday().getTime() < 0) {
                    date.setTime(player.getBirthday());
                    if (date.get(Calendar.YEAR) >= 2_000
                            && date.get(Calendar.YEAR) <= 3_000) {
                        result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    if (result == null) {
                        result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    int level = player.getExperience();
                    player.setLevel((int) (Math.sqrt(2500 + 200 * level) - 50) / 100);
                    int newUnderNextLevel = 50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience();
                    player.setUntilNextLevel(newUnderNextLevel);
                    result = new ResponseEntity<>(player1, HttpStatus.OK);
                }
            }
        }
        catch (DataIntegrityViolationException | NullPointerException e) {
            result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") String stringId) {
        try {
            Long id = Long.parseLong(stringId);
            if (id == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Player player = playerService.findById(id);
            if (player == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
         else {
                playerService.deleteById(id);
                return new ResponseEntity<Player>(HttpStatus.OK);
            }
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rest/players/count")
    public Long getCountPlayers(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "race", required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after", required = false) Long fromDate,
                                @RequestParam(value = "before", required = false) Long toDate,
                                @RequestParam(value = "banned", required = false) Boolean banned,
                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        Long count = playerService.findPlayerCount(name, title, race, profession, fromDate, toDate,
                banned, minExperience, maxExperience, minLevel, maxLevel);
        return count;
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") Long id,
                                               @RequestBody Player player) {
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String name = player.getName();
        String title = player.getTitle();
        Race race = player.getRace();
        Profession profession = player.getProfession();
        Date birthday = player.getBirthday();
        Object level = player.getLevel();
        Object exp = player.getExperience();
        Player player1;
        Calendar date = Calendar.getInstance();

        try {
            player1 = playerService.findById(id);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (name != null) {
            player1.setName(name);
        }
        if (title != null) {
            player1.setTitle(title);
        }
        if (race != null) {
            player1.setRace(race);
        }
        if (profession != null) {
            player1.setProfession(profession);
        }
        if (birthday != null) {
            if (birthday.getTime() < 0) {
                date.setTime(birthday);
                if (date.get(Calendar.YEAR) <= 2000 || date.get(Calendar.YEAR) >= 3000) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            player1.setBirthday(birthday);
        }
        if (player.getBanned() != null) {
            player1.setBanned(player.getBanned());
        }

        if (exp != null) {
            if (player.getExperience() > 10000000 || player.getExperience() < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            player1.setExperience(player.getExperience());
            int newLvl = (int) (Math.sqrt(2500 + 200 * (int) exp) - 50) / 100;
            player1.setLevel(newLvl);
        }
        else {
            int newLvl = (int) (Math.sqrt(2500 + 200 * player1.getExperience()) - 50) / 100;
            player1.setLevel(newLvl);
        }
        int lvl = player1.getLevel();
        int newUnderNextLevel = (50 * (lvl + 1) * (lvl + 2)) - player1.getExperience();
        player1.setUntilNextLevel(newUnderNextLevel);

        return new ResponseEntity<>(player1, HttpStatus.OK);
    }
}
