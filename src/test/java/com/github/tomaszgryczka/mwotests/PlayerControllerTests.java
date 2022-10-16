package com.github.tomaszgryczka.mwotests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(PlayerController.class)
@WebMvcTest(controllers = PlayerController.class)
@ContextConfiguration(classes = PlayerService.class)
public class PlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setPlayerRepository() {
        ReflectionTestUtils.setField(playerService, "playersDb", getPlayersData());
    }

    @Test
    public void should_ReturnPlayer_When_CreatePlayerRequestSent() throws Exception {
        // given
        final PlayerRequest playerRequest = PlayerRequest.builder()
                .coachId(1L)
                .country("Poland")
                .dateOfBirth(LocalDate.of(2000, 10, 10))
                .firstname("TEST_FIRSTNAME")
                .lastname("TEST_LASTNAME")
                .height(180.0)
                .weight(100.0)
                .build();

        // when
        final ResultActions response = mockMvc.perform(post("/players")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerRequest)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.coachId").value("1"))
                .andExpect(jsonPath("$.firstname").value("TEST_FIRSTNAME"))
                .andExpect(jsonPath("$.lastname").value("TEST_LASTNAME"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.dateOfBirth").value("2000-10-10"))
                .andExpect(jsonPath("$.height").value(180.0))
                .andExpect(jsonPath("$.weight").value(100.0));
    }

    @Test
    public void should_ReturnPlayer_When_GivenPlayerId() throws Exception {
        // given
        final long playerId = 3L;

        // when
        final ResultActions response = mockMvc.perform(get("/players/" + playerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(playerId))
                .andExpect(content().json(objectMapper.writeValueAsString(new Player(
                        3L,
                        3L,
                        "Stefan",
                        "Treneiro",
                        "Polska",
                        LocalDate.of(1969, 12, 2),
                        200.0,
                        180.0))));
    }

    @Test
    public void should_ReturnUpdatedPlayer_When_UpdatePlayerInfoRequestSent() throws Exception {
        // given
        final Player updatedInfoPlayer = Player.builder()
                .id(1L)
                .coachId(3L)
                .firstname("PRIVATE")
                .lastname("STATIC")
                .country("GERMANY")
                .dateOfBirth(LocalDate.of(2000, 12, 12))
                .height(165.0)
                .weight(165.0)
                .build();

        // when
        final ResultActions response = mockMvc.perform(put("/players")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInfoPlayer)));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedInfoPlayer)));
    }

    @Test
    public void should_DeletePlayer_When_DeletePlayerRequestSent() throws Exception {
        // given
        final long playerIdToDelete = 1L;

        // when
        final ResultActions response = mockMvc.perform(delete("/players/" + playerIdToDelete));

        // then
        final List<Player> playersDb = (List<Player>) ReflectionTestUtils.getField(playerService, "playersDb");
        final boolean result = playersDb.stream().noneMatch(player -> player.getId() == playerIdToDelete);

        response.andExpect(status().isNoContent());
        Assertions.assertTrue(result);
    }

    @Test
    public void should_FilterPlayersFromPoland_When_FilterByPolandRequestSent() throws Exception {
        // given
        final String country = "Polska";

        // when
        final ResultActions response = mockMvc.perform(get("/players/filter/" + country)
                .accept(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getPlayersLivingInPoland())));
    }

    private List<Player> getPlayersLivingInPoland() {
        return getPlayersData().stream()
                .filter(player -> player.getCountry().equals("Polska"))
                .collect(Collectors.toList());
    }

    private List<Player> getPlayersData() {
        return new ArrayList<>(Arrays.asList(
                new Player(
                        0L,
                        3L,
                        "Czesiek",
                        "Zwinny",
                        "Polska",
                        LocalDate.of(2000, 12, 12),
                        160.0,
                        80.0),
                new Player(
                        1L,
                        3L,
                        "Marcin",
                        "Powolny",
                        "Niemcy",
                        LocalDate.of(2015, 1, 1),
                        165.0,
                        80.2),
                new Player(
                        2L,
                        3L,
                        "Irena",
                        "Waleczna",
                        "Polska",
                        LocalDate.of(2002, 12, 14),
                        60.0,
                        180.0),
                new Player(
                        3L,
                        3L,
                        "Stefan",
                        "Treneiro",
                        "Polska",
                        LocalDate.of(1969, 12, 2),
                        200.0,
                        180.0)
        ));
    }
}
