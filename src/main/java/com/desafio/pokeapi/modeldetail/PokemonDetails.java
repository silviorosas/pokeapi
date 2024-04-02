package com.desafio.pokeapi.modeldetail;

import java.util.List;

import com.desafio.pokeapi.model.Ability;

import lombok.Data;

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
