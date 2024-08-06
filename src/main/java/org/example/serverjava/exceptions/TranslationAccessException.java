package org.example.serverjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ошибка доступа к ресурсу перевода ")
public class TranslationAccessException extends RuntimeException{
}

