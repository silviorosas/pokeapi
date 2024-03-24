package com.desafio.pokeapi.modelsPokemonDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestCombos {
    private Normal normal;
}
