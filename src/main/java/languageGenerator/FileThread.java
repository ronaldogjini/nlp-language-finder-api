package languageGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileThread implements Runnable {

    private Path filePath;
    Map<String, Long> languageModel;
    Map<String, Long> currentModel = new HashMap<>();
    private int ngram;
    private String text;

    public FileThread(Path filePath, int ngram, Map languageModel) {
        this.filePath = filePath;
        this.languageModel = languageModel;
        this.ngram = ngram;
    }

    public FileThread(String text, int ngram, Map languageModel) {
        this.text = text;
        this.languageModel = languageModel;
        this.ngram = ngram;
    }

    @Override
    public void run() {

        //gets the language model of the file
        if (text == null) {
            currentModel = LanguageModelProcessor.getLanguageModel(filePath, ngram);
        } else {
            try {
                currentModel = LanguageModelProcessor.getLanguageModelMystery(text, ngram);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //stores the file language model data into the actual language model
            currentModel.entrySet().stream()
                    .forEach(entry -> {
                        if (languageModel.containsKey(entry.getKey())) {
                            String key = entry.getKey();
                            languageModel.put(entry.getKey(), languageModel.get(key) + entry.getValue());
                        } else if(!languageModel.containsKey(entry.getKey())) {
                            languageModel.put(entry.getKey(), entry.getValue());
                        }
                    });
        }
    }



