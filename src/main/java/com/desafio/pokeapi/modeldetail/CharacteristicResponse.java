package com.desafio.pokeapi.modeldetail;

import java.util.List;

import com.desafio.pokeapi.model.NamedAPIResource;

import lombok.Data;

@Data
public class CharacteristicResponse {
    private int id;
    private int gene_modulo;
    private List<Integer> possible_values;
    private NamedAPIResource highest_stat;
    private List<Description> descriptions;
}