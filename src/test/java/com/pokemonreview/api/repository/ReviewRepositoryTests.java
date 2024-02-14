package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void ReviewRepository_SaveReview_ReturnSavedReview() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepository_FindAll_ReturnMoreThanOneReview() {
        Review review1 = Review.builder().title("title1").content("content1").stars(5).build();
        Review review2 = Review.builder().title("title2").content("content2").stars(10).build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviewList = reviewRepository.findAll();

        Assertions.assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_ReturnReview() {
        Review review1 = Review.builder().title("title1").content("content1").stars(5).build();

        reviewRepository.save(review1);

        Review reviewFetched = reviewRepository.findById(review1.getId()).orElse(null);

        Assertions.assertThat(reviewFetched).isNotNull();
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnReview() {
        Review review1 = Review.builder().title("title1").content("content1").stars(5).build();
        reviewRepository.save(review1);
        Review reviewSaved = reviewRepository.findById(review1.getId()).orElse(null);
        if(reviewSaved != null) {
            reviewSaved.setTitle("title2");
            reviewSaved.setStars(15);
        }

        Review reviewUpdated = reviewSaved != null ? reviewRepository.save(reviewSaved) : null;

        Assertions.assertThat(reviewUpdated).isNotNull();
    }

    @Test
    public void ReviewRepository_DeleteReview_ReturnNull() {
        Review review1 = Review.builder().title("title1").content("content1").stars(5).build();
        reviewRepository.save(review1);

        reviewRepository.deleteById(review1.getId());
        Review reviewDeleted = reviewRepository.findById(review1.getId()).orElse(null);

        Assertions.assertThat(reviewDeleted).isNull();
    }

    @Test
    public void ReviewRepository_FindByPokemonId_ReturnReview() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        pokemonRepository.save(pokemon);
        Review review = Review.builder().title("title1").content("content1").stars(5).build();
        review.setPokemon(pokemon);
        reviewRepository.save(review);

        List<Review> reviewList = reviewRepository.findByPokemonId(pokemon.getId());

        Assertions.assertThat(reviewList.size()).isEqualTo(1);
    }
}
