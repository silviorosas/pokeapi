package com.desafio.pokeapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.pokeapi.service.PokemonService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:4200")
class PokemonController {
    

    @Autowired
    private PokemonService pokemonService;

    @GetMapping
    @Operation(summary = "Obtiene todos los pokemons utilizando paginación", description = "Obtiene una página específica de Pokémon de la lista.")
    public ResponseEntity<?> getPokemonPage(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return pokemonService.getAllPokemonPage(page, pageSize);
    }

    @GetMapping("/data/{name}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener detalles completos de un Pokémon", description = "Obtiene información básica, descripción y lista de movimientos de un Pokémon por su nombre.")
    public ResponseEntity<?> getPokemonDetails(@PathVariable String name) {
        return pokemonService.getPokemonDetails(name);
    }




    
}
