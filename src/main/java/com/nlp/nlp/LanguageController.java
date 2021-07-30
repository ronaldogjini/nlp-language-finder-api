package com.nlp.nlp;

import languageGenerator.NLPMain;
import org.springframework.web.bind.annotation.*;

@RestController
public class LanguageController {

    @CrossOrigin
    @GetMapping("/languages")
    public Language calculateLanguages() {
        return new Language("hellooooo");
    }

    @CrossOrigin
    @PostMapping("/language")
    public Text calculateLanguage(@RequestBody String text) {
        String languageFound = NLPMain.findLanguage(text);
        if (languageFound == "en") {
            return new Text("English");
        } else if (languageFound == "de") {
            return new Text("German");
        } else if (languageFound == "fr") {
            return new Text("French");
        } else if (languageFound == "it") {
            return new Text("Italian");
        } else if (languageFound == "Ru") {
            return new Text("Russian");
        }
        return new Text("Write more");

    }
}