package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {

        //Arrange
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();

        //Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnsMoreThanOnePokemon() {
        Pokemon pokemon1 = Pokemon.builder().name("pikachu").type("electric").build();
        Pokemon pokemon2 = Pokemon.builder().name("bulbasaur").type("leaf").build();

        pokemonRepository.save(pokemon1);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();

        Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnsPokemon() {
        Pokemon pokemon1 = Pokemon.builder().name("pikachu").type("electric").build();

        pokemonRepository.save(pokemon1);

        Pokemon pokemon = pokemonRepository.findById(pokemon1.getId()).orElse(null);

        Assertions.assertThat(pokemon).isNotNull();
    }

    @Test
    public void PokemonRepository_FindByType_ReturnPokemon() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonFetched = pokemonRepository.findByType("electric").orElse(null);

        Assertions.assertThat(pokemonFetched).isNotNull();
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemon() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonSaved = pokemonRepository.findById(pokemon.getId()).orElse(null);
        if(pokemonSaved != null) {
            pokemonSaved.setName("Raichu");
            pokemonSaved.setType("Electric");
        }

        Pokemon updatedPokemon = pokemonSaved != null ? pokemonRepository.save(pokemonSaved) : null;

        Assertions.assertThat(updatedPokemon).isNotNull();
    }

    @Test
    public void PokemonRepository_DeletePokemon_ReturnNull() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());

        Pokemon pokemonFetched = pokemonRepository.findById(pokemon.getId()).orElse(null);

        Assertions.assertThat(pokemonFetched).isNull();
    }
}
