package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto() {
        Pokemon pokemon = Pokemon.builder().name("balbasaur").type("leaf").build();

        Mockito.when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto pokemonDto = PokemonDto.builder().name("pikachu").type("electric").build();

        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnsResponseDto() {
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        Mockito.when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse savePokemon = pokemonService.getAllPokemon(1, 10);

        Assertions.assertThat(savePokemon).isNotNull();
    }

    @Test
    public void PokemonService_FindById_ReturnPokemon() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        Mockito.when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        PokemonDto savedPokemon = pokemonService.getPokemonById(1);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_UpdatePokemon_ReturnPokemon() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        Mockito.when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        PokemonDto pokemonDto = PokemonDto.builder().name("balbasaur").type("leaf").build();
        Mockito.when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.updatePokemon(pokemonDto, 1);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_DeletePokemon_ReturnNull() {
        Pokemon pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        Mockito.when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        pokemonService.deletePokemonId(1);

        Mockito.verify(pokemonRepository, Mockito.times(1)).delete(pokemon);
    }
}
