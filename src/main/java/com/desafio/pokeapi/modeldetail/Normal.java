package com.desafio.pokeapi.modeldetail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Normal {
    private List<MoveLink> use_after;
    private List<MoveLink> use_before;
}