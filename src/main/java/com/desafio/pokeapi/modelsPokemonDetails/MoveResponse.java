package com.desafio.pokeapi.modelsPokemonDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveResponse {
    private int accuracy;
    private ContestCombos contest_combos;
}
