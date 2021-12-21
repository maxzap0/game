package com.game.controller;

import com.game.comporator.PlayerComparator;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.validate.PlayerValidCreate;
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
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize
    ){
        if (order==null) order = PlayerOrder.ID;
        List<Player> playerList = playerService.getAllPlayer();
        playerList = playerList.stream()
                .sorted( new PlayerComparator(order.getFieldName()){
                            @Override
                            public int compare(Player o1, Player o2) {
                                return super.compare(o1, o2);
                            }})
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
    public ResponseEntity<Integer> getCount(
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
            @RequestParam(required = false) Integer maxLevel
    ){
        List<Player> playerList = playerService.getAllPlayer();
        Integer count = (int) playerList.stream()
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
                .count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Player> createPlayer(
            @RequestBody Player player) {

        if (!PlayerValidCreate.validCreatePlayer(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!player.getBanned()) {
            player.setBanned(false);
        }
        player.setLevel(((int) (Math.sqrt((2500+(200*player.getExperience())))) - 50) / 100);
        player.setUntilNextLevel(50 * (player.getLevel()+1)*(player.getLevel()+2) - player.getExperience());
        playerService.savePlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(
            @RequestBody Player player,
            @PathVariable Long id
    ) {
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        /*if (player==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/

        if (!PlayerValidCreate.validUpdatePlayer(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player playerFromBase = playerService.getPlayerById(id);

        if (player.getName()!=null){
            playerFromBase.setName(player.getName());
        }
        if (player.getTitle()!=null){
            playerFromBase.setTitle(player.getTitle());
        }
        if (player.getRace()!=null){
            playerFromBase.setRace(player.getRace());
        }
        if (player.getProfession()!=null){
            playerFromBase.setProfession(player.getProfession());
        }
        if (player.getBirthday()!=null){
            playerFromBase.setBirthday(player.getBirthday());
        }
        if (player.getExperience()!=null){
            playerFromBase.setExperience(player.getExperience());
            playerFromBase.setLevel(((int) (Math.sqrt((2500+(200*player.getExperience())))) - 50) / 100);
            playerFromBase.setUntilNextLevel(50 * (playerFromBase.getLevel()+1)*(playerFromBase.getLevel()+2) - playerFromBase.getExperience());
        }
        if (player.getBanned()!=null){
            playerFromBase.setBanned(player.getBanned());
        }

        playerService.savePlayer(playerFromBase);

        return new ResponseEntity<>(playerFromBase, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable Long id){
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
