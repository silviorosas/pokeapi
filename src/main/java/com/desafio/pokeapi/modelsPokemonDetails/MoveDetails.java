package com.desafio.pokeapi.modelsPokemonDetails;

import lombok.Data;

@Data
public class MoveDetails {
    private int levelLearnedAt;
    private NamedAPIResource versionGroup;
    private NamedAPIResource moveLearnMethod;
}
