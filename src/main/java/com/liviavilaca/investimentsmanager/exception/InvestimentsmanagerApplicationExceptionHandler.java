package com.liviavilaca.investimentsmanager.exception;

import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class InvestimentsmanagerApplicationExceptionHandler {


    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<MessageResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder message = new StringBuilder();
        if(exception.getAllErrors()!=null) {
            message.append(exception.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining("; ")));
        }
        MessageResponseDTO response = MessageResponseDTO.builder()
                        .message(message.toString()).build();

        System.out.println("ERRO " + message.toString() );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    protected ResponseEntity<MessageResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
         MessageResponseDTO response = MessageResponseDTO.builder()
            .message("Required request body is missing").build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    protected ResponseEntity<MessageResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        MessageResponseDTO response = MessageResponseDTO.builder()
                .message("Invalid data! You may be trying to insert duplicate data.").build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    protected ResponseEntity<MessageResponseDTO> handleEntityNotFoundException(EntityNotFoundException exception) {
        MessageResponseDTO response = MessageResponseDTO.builder()
                .message(exception.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = { BadCredentialsException.class })
    protected ResponseEntity<MessageResponseDTO> handleBadCredentialsException(BadCredentialsException exception) {
        MessageResponseDTO response = MessageResponseDTO.builder()
                .message("Bad credentials").build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
