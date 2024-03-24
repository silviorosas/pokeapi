package com.desafio.pokeapi.modelsPokemonDetails;

import com.desafio.pokeapi.modelsAllPokemons.Ability;
import lombok.Data;

import java.util.List;

@Data
public class PokemonMoves {
    private String name;
    private String photo;
    private int weight;
    private String type;
    private List<Ability> abilities;
    private String description;
    private List<String> moves;
}
