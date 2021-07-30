package com.nlp.nlp;

import languageGenerator.NLPMain;
import org.springframework.web.bind.annotation.*;

@RestController
public class LanguageController {


    @GetMapping("/languages")
    public Language calculateLanguages() {
        return new Language("hellooooo");
    }


    @PostMapping("/language")
    public Text calculateLanguage(@RequestBody String text) {
        String languageFound = NLPMain.findLanguage(text);
        return new Text(languageFound);
    }
}