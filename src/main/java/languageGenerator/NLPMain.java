package languageGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NLPMain {

    public static Map<String, Double> languageDistanceMap = new HashMap<>();

    public static String findLanguage(String text) {

        Map<String, Long> mysteryTextModel = new HashMap<>();
        Map<String, Map<String, Long>> allModels = new HashMap<>();

        ExecutorService executorService = Executors.newCachedThreadPool();

        Path userPath = Paths.get("src/main/data/");

        int userNGram = 2;

        // executes a new folder or file thread, based on the condition
        try {
            Files.walk(userPath, 1)
                    .forEach(p -> {
                        if (Files.isDirectory(p) && !p.equals(userPath)) {
                            executorService.execute(new FolderThread(p, userNGram, allModels));
                        }
                        else if (Files.isRegularFile(p) && !p.equals(userPath)) {
                            System.out.println("WE AREE HERE");
                            System.out.println(p);

                            executorService.execute(new FileThread(text, userNGram, mysteryTextModel));
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // the executor no longer accepts new threads
        executorService.shutdown();

        // wait for the threads to finish, then execute the following streams
        try {
            if (executorService.awaitTermination(20, TimeUnit.SECONDS)) {

                allModels.entrySet().stream()
                        .forEach(entry -> System.out.println(entry.getValue()));

                System.out.println("Mystery Text: " + mysteryTextModel);

                allModels.entrySet().stream()
                        .forEach(map -> LanguageModelProcessor.calcDocumentDistance(map.getKey() ,map.getValue(), mysteryTextModel, languageDistanceMap));
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

        System.out.println("The document distance for each language is: " +languageDistanceMap);

        String model = LanguageModelProcessor.findSmallestLanguageDistance(languageDistanceMap);
        System.out.println("The language with the highest similarity to the mystery text is: " + model);

        return model;

    }
}

