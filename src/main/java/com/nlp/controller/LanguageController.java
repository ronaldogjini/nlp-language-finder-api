package com.nlp.controller;

import languageGenerator.NLPMain;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LanguageController {

    @CrossOrigin
    @PostMapping("/language")
    public Text calculateLanguage(@RequestBody String text) {
        String languageFound = NLPMain.findLanguage(text);
        Map<String, String> map = new HashMap<>();

        map.put("en", "English");
        map.put("fr", "French");
        map.put("it", "Italian");
        map.put("es", "Spanish");
        map.put("ru", "Russian");
        map.put("jp1", "Japanese");
        map.put("nl", "Dutch");
        map.put("pt", "Portuguese");
        map.put("gr", "Greek");
        map.put("de", "German");
        map.put("zh", "Chinese");


        return new Text(map.get(languageFound));

    }
}