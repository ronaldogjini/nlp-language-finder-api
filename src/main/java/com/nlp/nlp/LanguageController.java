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
        if (languageFound.equals("en")) {
            return new Text("English");
        } else if (languageFound.equals("de")) {
            return new Text("German");
        } else if (languageFound.equals("fr")) {
            return new Text("French");
        } else if (languageFound.equals("it")) {
            return new Text("Italian");
        } else if (languageFound.equals("ru")) {
            return new Text("Russian");
        }
        return new Text(languageFound);

    }
}