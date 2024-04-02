package com.desafio.pokeapi.modeldetail;




import com.desafio.pokeapi.model.NamedAPIResource;

import lombok.Data;

@Data
public class Description {
    private String description;
    private NamedAPIResource language;
}