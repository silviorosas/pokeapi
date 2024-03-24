package com.desafio.pokeapi.modelsAllPokemons;

import lombok.Data;

import java.util.List;

@Data
public class PokemonInfoBasic {
    private String name;
    private String photo;
    private int weight;
    private String type;
    private List<Ability> abilities;


}
