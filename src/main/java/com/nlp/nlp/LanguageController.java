package com.nlp.nlp;

import languageGenerator.NLPMain;
import org.springframework.web.bind.annotation.*;

@RestController
public class LanguageController {


    @GetMapping("/languages")
    public Language calculateLanguages() {
        return new Language("hellooooo");
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @PostMapping("/language")
    public Text calculateLanguage(@RequestBody String text) {
        String languageFound = NLPMain.findLanguage(text);
        return new Text(languageFound);
    }
}