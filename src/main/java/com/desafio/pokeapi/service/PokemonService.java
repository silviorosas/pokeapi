package com.desafio.pokeapi.service;

import com.desafio.pokeapi.modelsAllPokemons.Pokemon;
import com.desafio.pokeapi.modelsAllPokemons.PokemonApiResponse;
import com.desafio.pokeapi.modelsAllPokemons.PokemonInfoBasic;
import com.desafio.pokeapi.modelsAllPokemons.PokemonListResponse;
import com.desafio.pokeapi.modelsPokemonDetails.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {

    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String POKEAPI_CHARACTERISTIC_BASE_URL = "https://pokeapi.co/api/v2/characteristic/";


    @Autowired
    private RestTemplate restTemplate;


    public ResponseEntity<?> getAllPokemon() {
        List<Pokemon> allPokemon = new ArrayList<>();
        try {
            String url = POKEAPI_BASE_URL + "?limit=50";
            PokemonListResponse response = restTemplate.getForObject(url, PokemonListResponse.class);
            if (response != null && response.getResults() != null) {
                for (PokemonListResponse.PokemonEntry pokemonEntry : response.getResults()) {
                    PokemonApiResponse pokemonApiResponse = restTemplate.getForObject(pokemonEntry.getUrl(), PokemonApiResponse.class);
                    if (pokemonApiResponse != null) {
                        allPokemon.add(mapToPokemon(pokemonApiResponse));
                    }
                }
            }
            return ResponseEntity.ok().body(allPokemon);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    private Pokemon mapToPokemon(PokemonApiResponse response) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(response.getName());
        pokemon.setPhoto(response.getSprites().getFrontDefault());
        pokemon.setWeight(response.getWeight());
        pokemon.setType(response.getTypes().get(0).getType().getName());
        pokemon.setAbilities(response.getAbilities());
        return pokemon;
    }

    public ResponseEntity<?> getPokemonInfoBasic(String name) {
        try {
            String url = POKEAPI_BASE_URL + name.toLowerCase();
            PokemonApiResponse response = restTemplate.getForObject(url, PokemonApiResponse.class);
            if (response != null) {
                PokemonInfoBasic pokemon = mapToPokemonBasic(response);
                return ResponseEntity.ok().body(pokemon);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    private PokemonInfoBasic mapToPokemonBasic(PokemonApiResponse response) {
        PokemonInfoBasic pokemon = new PokemonInfoBasic();
        pokemon.setName(response.getName());
        pokemon.setPhoto(response.getSprites().getFrontDefault());
        pokemon.setWeight(response.getWeight());
        pokemon.setType(response.getTypes().get(0).getType().getName());
        pokemon.setAbilities(response.getAbilities());
        return pokemon;
    }


    public ResponseEntity<?> getPokemonByInfoBasicAndDescription(String name) {
        try {
            String url = POKEAPI_BASE_URL + name.toLowerCase();
            PokemonApiResponse response = restTemplate.getForObject(url, PokemonApiResponse.class);
            if (response != null) {
                Pokemon pokemon = mapToPokemon(response);
                // Obtener información de la característica del Pokémon
                CharacteristicResponse characteristicResponse = restTemplate.getForObject(POKEAPI_CHARACTERISTIC_BASE_URL + response.getId(), CharacteristicResponse.class);
                if (characteristicResponse != null) {
                    pokemon.setDescription(getDescriptionInSpanish(characteristicResponse));
                }
                return ResponseEntity.ok().body(pokemon);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }


    private String getDescriptionInSpanish(CharacteristicResponse characteristicResponse) {
        if (characteristicResponse != null && characteristicResponse.getDescriptions() != null) {
            for (Description description : characteristicResponse.getDescriptions()) {
                NamedAPIResource language = description.getLanguage();
                if (language != null && "es".equalsIgnoreCase(language.getName())) {
                    return description.getDescription();
                }
            }
        }
        return null;
    }

    //trae solo los moves por id
    public ResponseEntity<List<String>> getPokemonMovesById(String id) {
        try {
            // Construir la URL para obtener los detalles del movimiento
            String moveUrl = "https://pokeapi.co/api/v2/move/" + id;

            // Realizar la solicitud a la PokeAPI para obtener los detalles del movimiento
            ResponseEntity<MoveResponse> moveResponseEntity = restTemplate.getForEntity(moveUrl, MoveResponse.class);

            if (moveResponseEntity.getStatusCode().is2xxSuccessful()) {
                MoveResponse moveResponse = moveResponseEntity.getBody();

                // Extraer los nombres de los movimientos
                List<String> moves = extractMoves(moveResponse);

                // Devolver la lista de nombres de movimientos en la respuesta
                return ResponseEntity.ok().body(moves);
            } else {
                // Manejar el caso en que la solicitud no fue exitosa
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception ex) {
            // Manejar cualquier excepción que pueda ocurrir durante la solicitud
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private List<String> extractMoves(MoveResponse moveResponse) {
        List<String> moves = new ArrayList<>();

        if (moveResponse != null && moveResponse.getContest_combos() != null) {
            ContestCombos contestCombos = moveResponse.getContest_combos();

            if (contestCombos.getNormal() != null) {
                List<MoveLink> useAfter = contestCombos.getNormal().getUse_after();
                List<MoveLink> useBefore = contestCombos.getNormal().getUse_before();

                if (useAfter != null) {
                    for (MoveLink moveLink : useAfter) {
                        moves.add(moveLink.getName());
                    }
                }

                if (useBefore != null) {
                    for (MoveLink moveLink : useBefore) {
                        moves.add(moveLink.getName());
                    }
                }
            }
        }

        return moves;
    }


    //trae info basica, descripcion y moves
    public ResponseEntity<?> getPokemonDetails(String name) {
        try {
            String url = POKEAPI_BASE_URL + name.toLowerCase();
            PokemonApiResponse response = restTemplate.getForObject(url, PokemonApiResponse.class);
            if (response != null) {
                Pokemon pokemon = mapToPokemon(response);

                ResponseEntity<List<String>> movesResponse = getPokemonMovesById(response.getId().toString());
                if (movesResponse.getStatusCode().is2xxSuccessful()) {
                    List<String> moves = movesResponse.getBody();
                    PokemonDetails pokemonDetails = mapToPokemonDetails(response, moves);
                    CharacteristicResponse characteristicResponse = restTemplate.getForObject(POKEAPI_CHARACTERISTIC_BASE_URL + response.getId(), CharacteristicResponse.class);
                    if (characteristicResponse != null) {
                        pokemonDetails.setDescription(getDescriptionInSpanish(characteristicResponse));
                    }
                    return ResponseEntity.ok().body(pokemonDetails);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los movimientos del Pokémon");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    private PokemonDetails mapToPokemonDetails(PokemonApiResponse pokemonResponse, List<String> moves) {
        PokemonDetails pokemonDetails = new PokemonDetails();
        pokemonDetails.setName(pokemonResponse.getName());
        pokemonDetails.setPhoto(pokemonResponse.getSprites().getFrontDefault());
        pokemonDetails.setWeight(pokemonResponse.getWeight());
        pokemonDetails.setType(pokemonResponse.getTypes().get(0).getType().getName());
        pokemonDetails.setAbilities(pokemonResponse.getAbilities());
        pokemonDetails.setDescription("Se caracteriza por su cuerpo resistente");
        pokemonDetails.setMoves(moves);
        return pokemonDetails;
    }








}

