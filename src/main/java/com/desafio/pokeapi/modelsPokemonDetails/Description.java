package com.desafio.pokeapi.modelsPokemonDetails;

import lombok.Data;

@Data
public class Description {
    private String description;
    private NamedAPIResource language;
}
