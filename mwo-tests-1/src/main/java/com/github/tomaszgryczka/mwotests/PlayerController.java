package com.github.tomaszgryczka.mwotests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/{playerId}")
    public Player getPlayer(@PathVariable Long playerId) {
        return playerService.findPlayerById(playerId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Player createPlayer(@RequestBody PlayerRequest playerRequest) {
        return playerService.save(playerRequest);
    }

    @PutMapping
    public Player updatePlayer(@RequestBody Player player) {
        return playerService.setPlayerInfoById(player);
    }

    @DeleteMapping("/{playerId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayerById(playerId);
    }

    @GetMapping("/filter/{country}")
    public List<Player> filterByCountry(@PathVariable String country) {
        return playerService.findPlayersByCountry(country);
    }
}
