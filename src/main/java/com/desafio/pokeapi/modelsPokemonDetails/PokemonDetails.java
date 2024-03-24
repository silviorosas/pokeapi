package com.desafio.pokeapi.modelsPokemonDetails;

import com.desafio.pokeapi.modelsAllPokemons.Ability;
import com.desafio.pokeapi.modelsAllPokemons.Pokemon;
import lombok.Data;

import java.util.List;

@Data
public class PokemonDetails {
    private String name;
    private String photo;
    private int weight;
    private String type;
    private List<Ability> abilities;
    private String description;
    private List<String> moves;
}
