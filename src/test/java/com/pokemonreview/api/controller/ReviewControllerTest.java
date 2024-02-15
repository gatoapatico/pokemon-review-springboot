package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.ReviewController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.ReviewService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @MockBean
    private ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;
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
    public void ReviewController_CreateReview_ReturnCreated() throws Exception {
        Mockito.when(reviewService.createReview(Mockito.eq(pokemonDto.getId()), Mockito.any(ReviewDto.class))).thenAnswer(invoke -> invoke.getArgument(1));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/pokemon/"+pokemonDto.getId()+"/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())));
    }

    @Test
    public void ReviewController_GetReviewByPokemonId_ReturnList() throws Exception {
        Mockito.when(reviewService.getReviewsByPokemonId(pokemonDto.getId())).thenReturn(Arrays.asList(reviewDto));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/pokemon/"+pokemonDto.getId()+"/reviews"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(Arrays.asList(reviewDto).size())));
    }

    @Test
    public void ReviewController_GetReviewById_ReturnReviewDto() throws Exception {
        Mockito.when(reviewService.getReviewById(review.getId(), pokemonDto.getId())).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/pokemon/"+pokemonDto.getId()+"/reviews/"+review.getId()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())));
    }

    @Test
    public void ReviewController_UpdateReview_ReturnReviewDto() throws Exception {
        Mockito.when(reviewService.updateReview(Mockito.eq(pokemon.getId()), Mockito.eq(review.getId()), Mockito.any(ReviewDto.class))).thenAnswer(invoke -> invoke.getArgument(2));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/pokemon/"+pokemon.getId()+"/reviews/"+review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())));
    }

    @Test
    public void ReviewController_DeleteReview_ReturnNull() throws Exception {
        Mockito.doNothing().when(reviewService).deleteReview(pokemon.getId(), reviewDto.getId());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/pokemon/"+pokemon.getId()+"/reviews/"+reviewDto.getId()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
