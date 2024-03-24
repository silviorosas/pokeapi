package com.desafio.pokeapi.modelsAllPokemons;

import com.desafio.pokeapi.modelsPokemonDetails.NamedAPIResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class PokemonApiResponse {
    private Long id;
    private String name;
    private Sprites sprites;
    private int weight;
    private List<Ability> abilities;
    private List<TypeEntry> types;
    private String movesUrl;
    private List<NamedAPIResource> moves;



    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoveEntry {
        private NamedAPIResource move;
    }
}
