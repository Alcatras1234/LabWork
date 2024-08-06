package org.example.serverjava.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.serverjava.Services.ActionService;
import org.example.serverjava.Services.TranslateService;
import org.example.serverjava.entities.Action;
import org.example.serverjava.entities.InputData;
import org.example.serverjava.exceptions.SourceLanguageNotFoundException;
import org.example.serverjava.exceptions.TranslationAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/api")
public class ActionController {
    @Autowired
    private ActionService actionService;

    @Autowired
    private TranslateService translateService;

    @GetMapping("/actions")
    public List<Action> getAll() {
        return actionService.getAllActions();
    }

    @PostMapping("/translate")
    public ResponseEntity<String> translateText(@RequestBody InputData inputData, HttpServletRequest request) {
        Action action;
        action = new Action();

        String translatedText = translateService.translate(inputData.getSourceLanguage(), inputData.getTargetLanguage(), inputData.getText());

        action.setIp(request.getRemoteAddr());
        action.setInputText(inputData.getText());
        action.setOutputText(translatedText);

        actionService.saveAction(action);
        return ResponseEntity.ok(translatedText);
    }




}
