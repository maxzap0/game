package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            @RequestParam(required = false) Profession profession
    ){
        List<Player> playerList = playerService.getAllPlayer();

        if(name!=null) {
            playerList = playerList.stream().filter(p->p.getName().contains(name)).collect(Collectors.toList());
        }
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
