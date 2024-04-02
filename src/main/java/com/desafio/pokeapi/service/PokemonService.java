package com.desafio.pokeapi.service;

import com.desafio.pokeapi.model.NamedAPIResource;
import com.desafio.pokeapi.model.Pokemon;
import com.desafio.pokeapi.model.PokemonDetailsApiResponse;
import com.desafio.pokeapi.model.PokemonListApiResponse;
import com.desafio.pokeapi.modeldetail.CharacteristicResponse;
import com.desafio.pokeapi.modeldetail.ContestCombos;
import com.desafio.pokeapi.modeldetail.Description;
import com.desafio.pokeapi.modeldetail.MoveLink;
import com.desafio.pokeapi.modeldetail.MoveResponse;

import com.desafio.pokeapi.modeldetail.PokemonDetails;
import com.desafio.pokeapi.utils.DescripcionNotFoundException;
import com.desafio.pokeapi.utils.MovimientosNoEncontradosException;
import com.desafio.pokeapi.utils.RecursoNoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PokemonService {

    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String POKEAPI_CHARACTERISTIC_BASE_URL = "https://pokeapi.co/api/v2/characteristic/";


    @Autowired
    private RestTemplate restTemplate;


// Método para obtener todos los Pokémon
public ResponseEntity<?> getAllPokemon() {
    // Lista para almacenar todos los Pokémon
    List<Pokemon> allPokemon = new ArrayList<>();
    try {
        // Construir la URL para obtener los Pokémon
        String url = POKEAPI_BASE_URL + "?limit=50";
// realiza una solicitud HTTP GET a una URL especificada y espera recibir una respuesta en forma de un objeto de la clase PokemonListResponse.
        PokemonListApiResponse response = restTemplate.getForObject(url, PokemonListApiResponse.class);
        // Verificar si la respuesta y los resultados no son nulos
        if (response != null && response.getResults() != null) {
            // Iterar sobre los resultados de la lista de Pokémon
            for (PokemonListApiResponse.PokemonEntry pokemonEntry : response.getResults()) {
                // Realizar una solicitud HTTP GET para obtener los detalles de cada Pokémon
                PokemonDetailsApiResponse pokemonApiResponse = restTemplate.getForObject(pokemonEntry.getUrl(), PokemonDetailsApiResponse.class);
                // Verificar si la respuesta no es nula
                if (pokemonApiResponse != null) {
                    // Mapear la respuesta a un objeto de tipo Pokémon y agregarlo a la lista
                    allPokemon.add(mapToPokemon(pokemonApiResponse));
                }
            }
        }
        // Devolver una respuesta exitosa con la lista de todos los Pokémon
        return ResponseEntity.ok().body(allPokemon);
    } catch (Exception ex) {
        // En caso de error, devolver una respuesta de error interno del servidor
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
    }
}


// Método para obtener todos los Pokémon paginados
public ResponseEntity<?> getAllPokemonPage(int page, int pageSize) {
    // Lista para almacenar todos los Pokémon
    List<Pokemon> allPokemon = new ArrayList<>();
    try {
        // Calcular el offset basado en el número de página y el tamaño de la página
        int offset = (page - 1) * pageSize;
        
        // Construir la URL con el offset y el tamaño de la página
        String url = POKEAPI_BASE_URL + "?offset=" + offset + "&limit=" + pageSize;
        
        // Realizar una solicitud HTTP GET para obtener la lista de Pokémon
        ResponseEntity<PokemonListApiResponse> responseEntity = restTemplate.getForEntity(url, PokemonListApiResponse.class);
        PokemonListApiResponse response = responseEntity.getBody();
        
        // Verificar si la respuesta y los resultados no son nulos
        if (response != null && response.getResults() != null) {
            // Iterar sobre los resultados de la lista de Pokémon
            for (PokemonListApiResponse.PokemonEntry pokemonEntry : response.getResults()) {
                // Realizar una solicitud HTTP GET para obtener los detalles de cada Pokémon
                PokemonDetailsApiResponse pokemonApiResponse = restTemplate.getForObject(pokemonEntry.getUrl(), PokemonDetailsApiResponse.class);
                // Verificar si la respuesta no es nula
                if (pokemonApiResponse != null) {
                    // Mapear la respuesta a un objeto de tipo Pokémon y agregarlo a la lista
                    allPokemon.add(mapToPokemon(pokemonApiResponse));
                }
            }
        }
        // Devolver una respuesta exitosa con la lista de todos los Pokémon
        return ResponseEntity.ok().body(allPokemon);
    } catch (Exception ex) {
        // En caso de error, devolver una respuesta de error interno del servidor
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
    }
}

    
    private Pokemon mapToPokemon(PokemonDetailsApiResponse response) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(response.getName());
        pokemon.setPhoto(response.getSprites().getFrontDefault());
        pokemon.setWeight(response.getWeight());
        pokemon.setType(response.getTypes().get(0).getType().getName());
        pokemon.setAbilities(response.getAbilities());
        return pokemon;
    }


     //trae info basica, descripcion y moves
    // Método para obtener los detalles de un Pokémon, incluyendo información básica, descripción y movimientos
public ResponseEntity<?> getPokemonDetails(String name) {
    try {
        // Construye la URL para la solicitud a la PokeAPI usando el nombre del Pokémon en minúsculas
        String url = POKEAPI_BASE_URL + name.toLowerCase();
        
        // Realiza una solicitud GET para obtener los detalles del Pokémon
        PokemonDetailsApiResponse response = restTemplate.getForObject(url, PokemonDetailsApiResponse.class);
        
        // Verifica si la respuesta no es nula
        if (response != null) {
            // Realiza una solicitud GET para obtener los movimientos del Pokémon
            ResponseEntity<List<String>> movesResponse = getPokemonMovesById(response.getId().toString());
            
            // Verifica si la solicitud de movimientos fue exitosa (código de estado 2xx)
            if (movesResponse.getStatusCode().is2xxSuccessful()) {
                // Obtiene los movimientos del cuerpo de la respuesta
                List<String> moves = movesResponse.getBody();
                
                // Mapea los detalles del Pokémon utilizando la información básica y los movimientos
                PokemonDetails pokemonDetails = mapToPokemonDetails(response, moves);
                
                // Realiza una solicitud GET para obtener las características del Pokémon
                CharacteristicResponse characteristicResponse = restTemplate.getForObject(POKEAPI_CHARACTERISTIC_BASE_URL + response.getId(), CharacteristicResponse.class);
                
                // Verifica si la respuesta de las características no es nula
                if (characteristicResponse != null) {
                    // Obtiene la descripción en español de las características y la asigna al Pokémon
                    pokemonDetails.setDescription(getDescriptionInSpanish(characteristicResponse));
                }
                
                // Devuelve una respuesta exitosa con los detalles del Pokémon
                return ResponseEntity.ok().body(pokemonDetails);
            } else {
                // Lanza una excepción personalizada si no se pudieron obtener los movimientos del Pokémon
                throw new MovimientosNoEncontradosException("Movimientos no encontrados");
            }
        } else {
            // Lanza una excepción si no se encontró la descripción del Pokémon
            throw new DescripcionNotFoundException("Descripción no encontrada");
        }
    } catch (HttpClientErrorException.NotFound ex) {
        // Lanza una excepción si no se encuentra el recurso del Pokémon
        throw new RecursoNoEncontradoException("Recurso no encontrado");
    } catch (Exception ex) {
        // Devuelve un error interno del servidor si ocurre una excepción no controlada
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
    }
}

    private PokemonDetails mapToPokemonDetails(PokemonDetailsApiResponse pokemonResponse, List<String> moves) {
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

      private String getDescriptionInSpanish(CharacteristicResponse characteristicResponse) {
        if (characteristicResponse != null && characteristicResponse.getDescriptions() != null) {
            for (Description description : characteristicResponse.getDescriptions()) {
                NamedAPIResource language = description.getLanguage();
                if (language != null && "es".equalsIgnoreCase(language.getName())) {
                    return description.getDescription();
                }
            }
        }
       throw new DescripcionNotFoundException("La descripción en español no fue encontrada.");
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

}

