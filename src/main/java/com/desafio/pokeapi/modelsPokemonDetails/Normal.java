package com.desafio.pokeapi.modelsPokemonDetails;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Normal {
    private List<MoveLink> use_after;
    private List<MoveLink> use_before;
}
