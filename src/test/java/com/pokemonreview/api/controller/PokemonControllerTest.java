package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;


@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PokemonService pokemonService;
    @Autowired
    private ObjectMapper objectMapper;

    private Pokemon pokemon;
    private PokemonDto pokemonDto;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        pokemonDto = PokemonDto.builder().name("balbasaur").type("leaf").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("review title").content("test content").stars(10).build();
    }

    @Test
    public void PokemonController_CreatePokemon_ReturnCreated() throws Exception {
        Mockito.when(pokemonService.createPokemon(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is("leaf")));
    }

    @Test
    public void PokemonController_GetAllPokemon_ReturnResponseDto() throws Exception {
        PokemonResponse responseDto = PokemonResponse.builder().pageSize(10).last(true).pageNo(1).content(Arrays.asList(pokemonDto)).build();
        Mockito.when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/pokemon")
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    public void PokemonController_GetPokemonDetail_ReturnPokemonDto() throws Exception {
        Mockito.when(pokemonService.getPokemonById(pokemonDto.getId())).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/pokemon/"+pokemonDto.getId()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
    }

    @Test
    public void PokemonController_UpdatePokemon_ReturnPokemonDto() throws Exception {
        Mockito.when(pokemonService.updatePokemon(Mockito.any(PokemonDto.class), Mockito.anyInt())).thenAnswer(invoke -> invoke.getArgument(0));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/pokemon/"+pokemonDto.getId()+"/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void PokemonController_DeletePokemon_ReturnString() throws Exception {
        Mockito.doNothing().when(pokemonService).deletePokemonId(Mockito.anyInt());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/pokemon/"+pokemonDto.getId()+"/delete"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
