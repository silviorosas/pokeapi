package com.desafio.pokeapi.modelsAllPokemons;

import lombok.Data;

@Data
public class Sprites {
    private String front_default;

    public String getFrontDefault() {
        return front_default;
    }

    public void setFrontDefault(String front_default) {
        this.front_default = front_default;
    }
}
