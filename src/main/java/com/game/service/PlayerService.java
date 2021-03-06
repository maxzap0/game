package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public List<Player> getAllPlayer() {
        return playerRepository.findAll();
    }

    @Transactional
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Transactional
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public Boolean exists(Long id) {
        return playerRepository.existsById(id);
    }
}
