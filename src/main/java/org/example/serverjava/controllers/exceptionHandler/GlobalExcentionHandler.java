package org.example.serverjava.controllers.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.serverjava.exceptions.SourceLanguageNotFoundException;
import org.example.serverjava.exceptions.TargetLanguageNotFoundException;
import org.example.serverjava.exceptions.TranslationAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

@ControllerAdvice
public class GlobalExcentionHandler {
    @ExceptionHandler(SourceLanguageNotFoundException.class)
    public ResponseEntity<String> handleSourceLanguageNotFoundException(SourceLanguageNotFoundException ex) {
        return new ResponseEntity<>("Не найден язык исходного сообщения", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TargetLanguageNotFoundException.class)
    public ResponseEntity<String> handleSourceLanguageNotFoundException(TargetLanguageNotFoundException ex) {
        return new ResponseEntity<>("Не найден язык для выходного сообщения", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TranslationAccessException.class)
    public ResponseEntity<String> handleTranslationAccessException(TranslationAccessException ex) {
        return new ResponseEntity<>("Ошибка доступа к ресурсу перевода", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleTranslationAccessException(RuntimeException ex) {
        return new ResponseEntity<>("Ошибка обработки JSON-ответа: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e) {
        return new ResponseEntity<>("Ошибка HTTP запроса: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<String> handleTranslationAccessException(InterruptedException ex) {
        return new ResponseEntity<>("Задача была прервана.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<String> handleTranslationAccessException(ExecutionException ex) {
        return new ResponseEntity<>("Ошибка выполнения задачи.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<String> handleTranslationAccessException(UnsupportedEncodingException ex) {
        return new ResponseEntity<>("Ошибка выполнения задачи.", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Неизвестная ошибка: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
