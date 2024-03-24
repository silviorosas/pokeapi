package com.desafio.pokeapi.modelsAllPokemons;

import lombok.Data;

import java.util.List;

@Data
public class PokemonListResponse {

    private List<PokemonEntry> results;



    @Data
    public static class PokemonEntry {
        private String name;
        private String url;


    }
}
