package com.desafio.pokeapi.modelsPokemonDetails;

import lombok.Data;

import java.util.List;

@Data
public class CharacteristicResponse {
    private int id;
    private int gene_modulo;
    private List<Integer> possible_values;
    private NamedAPIResource highest_stat;
    private List<Description> descriptions;
}
