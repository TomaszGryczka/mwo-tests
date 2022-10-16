package com.github.tomaszgryczka.mwotests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlayerService {
    private final List<Player> playersDb = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    public Player save(final PlayerRequest playerRequest) {

        final long lastPlayerId = playersDb.size() == 0 ? 0 : playersDb.get(playersDb.size() - 1).getId();

        final Player newPlayer = Player.builder()
                .id(lastPlayerId + 1)
                .coachId(playerRequest.getCoachId())
                .country(playerRequest.getCountry())
                .dateOfBirth(playerRequest.getDateOfBirth())
                .firstname(playerRequest.getFirstname())
                .lastname(playerRequest.getLastname())
                .height(playerRequest.getHeight())
                .weight(playerRequest.getWeight())
                .build();

        playersDb.add(newPlayer);

        return newPlayer;
    }

    public Player findPlayerById(final Long id) {
        return playersDb.stream()
                .filter(player -> player.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Player> findPlayersByCountry(final String country) {
        return playersDb.stream()
                .filter(player -> player.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    public Player setPlayerInfoById(final Player player) {
        final long playerId = player.getId();

        final long oldPlayerIndex = playersDb.stream()
                .filter(p -> p.getId() == playerId)
                .findFirst()
                .map(playersDb::indexOf)
                .orElseThrow(IllegalArgumentException::new);

        logger.info("Updating player with id: {}", oldPlayerIndex);
        playersDb.set(Math.toIntExact(oldPlayerIndex), player);

        return playersDb.get(Math.toIntExact(oldPlayerIndex));
    }

    public void deletePlayerById(final Long playerId) {
        playersDb.removeIf(player -> player.getId() == playerId);
    }
}
