package com.desafio.pokeapi.modelsPokemonDetails;

import com.desafio.pokeapi.modelsAllPokemons.Ability;
import com.desafio.pokeapi.modelsAllPokemons.Sprites;
import com.desafio.pokeapi.modelsAllPokemons.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonMovesResponse {
    private List<MoveEntry> moves;
}