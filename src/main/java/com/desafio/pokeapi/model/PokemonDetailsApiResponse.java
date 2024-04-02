package com.desafio.pokeapi.model;


import lombok.Data;

import java.util.List;

@Data
public class PokemonDetailsApiResponse {
    private Long id;
    private String name;
    private Sprites sprites;
    private int weight;
    private List<Ability> abilities;
    private List<TypeEntry> types;
    private String movesUrl;
    private List<NamedAPIResource> moves;


    /*@Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoveEntry {
        private NamedAPIResource move;
    }*/
}
