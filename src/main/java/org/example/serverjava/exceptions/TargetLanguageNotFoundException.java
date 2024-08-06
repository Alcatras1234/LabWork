package org.example.serverjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Не найден язык для выходного сообщения")
public class TargetLanguageNotFoundException extends RuntimeException{
}
