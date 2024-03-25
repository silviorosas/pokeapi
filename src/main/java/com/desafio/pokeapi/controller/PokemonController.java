package com.desafio.pokeapi.controller;



import com.desafio.pokeapi.service.PokemonService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;


import java.util.List;



@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:4200")
class PokemonController {

    private final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final String POKEAPI_CHARACTERISTIC_BASE_URL = "https://pokeapi.co/api/v2/characteristic/";


    private final RestTemplate restTemplate;

    public PokemonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private PokemonService pokemonService;

    // Trae los 20 primeros pokemones
    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener los 20 primeros Pokémon",
            description = "Obtiene los 20 primeros Pokémon de la lista.")
    public ResponseEntity<?> getAllPokemon() {
        return pokemonService.getAllPokemon();
    }

    // Trae información básica del Pokémon
    @GetMapping("/basic/{name}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener información básica de un Pokémon",
            description = "Obtiene información básica de un Pokémon por su nombre.")
    public ResponseEntity<?> getPokemonInfoBasic(@PathVariable String name) {
        return pokemonService.getPokemonInfoBasic(name);
    }


    // Trae info básica + descripción en español
    @GetMapping("/description/{name}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener información básica y descripción de un Pokémon",
            description = "Obtiene información básica y la descripción en español de un Pokémon por su nombre.")
    public ResponseEntity<?> getPokemonByInfoBasicAndDescription(@PathVariable String name) {
        return pokemonService.getPokemonByInfoBasicAndDescription(name);
    }


    // Trae solo los moves por id
    @GetMapping("/moves/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener movimientos de un Pokémon por su ID",
            description = "Obtiene los movimientos de un Pokémon por su ID.")
    public ResponseEntity<List<String>> getPokemonMovesById(@PathVariable String id) {
        return pokemonService.getPokemonMovesById(id);
    }


    //trae info basica, descrpcion y lista de moves
    @GetMapping("/data/{name}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener detalles completos de un Pokémon",
            description = "Obtiene información básica, descripción y lista de movimientos de un Pokémon por su nombre.")
    public ResponseEntity<?> getPokemonDetails(@PathVariable String name) {
        return pokemonService.getPokemonDetails(name);
    }





}




