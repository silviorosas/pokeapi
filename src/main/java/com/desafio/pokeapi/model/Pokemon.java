package com.desafio.pokeapi.model;

import lombok.Data;

import java.util.List;

@Data
public class Pokemon {
    private String name;
    private String photo;
    private int weight;
    private String type;
    private List<Ability> abilities;

}
