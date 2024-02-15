package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("review title").content("test content").stars(10).build();
    }

    @Test
    public void ReviewService_CreateReview_ReturnReviewDto() {
        Mockito.when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(pokemon.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewByPokemonId_ReturnList() {
        Mockito.when(reviewRepository.findByPokemonId(1)).thenReturn(Arrays.asList(review));

        List<ReviewDto> reviewDtoList = reviewService.getReviewsByPokemonId(1);

        Assertions.assertThat(reviewDtoList.size()).isEqualTo(1);
    }

    @Test
    public void ReviewService_GetReviewById_ReturnReviewDto() {
        Mockito.when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        Mockito.when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        review.setPokemon(pokemon);

        ReviewDto savedReview = reviewService.getReviewById(review.getId(), pokemon.getId());

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_UpdateReview_ReturnReviewDto() {
        Mockito.when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        Mockito.when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        review.setPokemon(pokemon);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.updateReview(pokemon.getId(), review.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_DeleteReview_ReturnNull() {
        Mockito.when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        Mockito.when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        review.setPokemon(pokemon);

        reviewService.deleteReview(pokemon.getId(), review.getId());

        Mockito.verify(reviewRepository, Mockito.times(1)).delete(review);
    }
}
