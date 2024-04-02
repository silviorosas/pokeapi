package com.desafio.pokeapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DescripcionNotFoundException.class)
    public ResponseEntity<Object> handleDescripcionNotFoundException(DescripcionNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<?> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MovimientosNoEncontradosException.class)
    public ResponseEntity<Object> handleMovimientosNoEncontradosException(MovimientosNoEncontradosException ex) {
       
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}
}
