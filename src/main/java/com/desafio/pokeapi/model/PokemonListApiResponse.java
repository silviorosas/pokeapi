package com.desafio.pokeapi.model;

import lombok.Data;

import java.util.List;

@Data
public class PokemonListApiResponse {
    private List<PokemonEntry> results;

    @Data
    public static class PokemonEntry {
        private String name;
        private String url;
    }

}
