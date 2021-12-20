package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayer(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) PlayerOrder playerOrder,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize
    ){
        List<Player> playerList = playerService.getAllPlayer();

        playerList = playerList.stream()
                .filter(player -> name==null||player.getName().contains(name))
                .filter(player -> title==null||player.getTitle().contains(title))
                .filter(player -> race==null||player.getRace()==race)
                .filter(player -> profession==null||player.getProfession()==profession)
                .filter(player -> after==null||player.getBirthday().getTime()>=after)
                .filter(player -> before==null||player.getBirthday().getTime()<=before)
                .filter(player -> banned==null||player.getBanned()==banned)
                .filter(player -> minExperience==null||player.getExperience()>=minExperience)
                .filter(player -> maxExperience==null||player.getExperience()<=maxExperience)
                .filter(player -> minLevel==null||player.getLevel()>=minLevel)
                .filter(player -> maxLevel==null||player.getLevel()<=maxLevel)
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id){
        if (id>playerService.getCount()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player player = playerService.getPlayerById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount(){
        Integer count;
        count = playerService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable Long id){
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id>playerService.getCount()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
