package com.desafio.pokeapi.modeldetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestCombos {
    private Normal normal;
}