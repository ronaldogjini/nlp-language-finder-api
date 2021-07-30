package languageGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FolderThread implements Runnable {

    private Path folderPath;
    private ExecutorService executorService = Executors.newCachedThreadPool();

   private String langCode;
   Map<String, Map<String, Long>> allModels;
   Map<String, Long> languageModel = new HashMap<>();
   private int ngram;

    public FolderThread(Path folderPath, int ngram, Map allModels){
        this.folderPath = folderPath;
        this.allModels = allModels;
        this.ngram = ngram;
        this.langCode = folderPath.getFileName().toString();
    }

    @Override
    public void run() {

        // creates and executes new file threads
        try {
            Files.walk(Paths.get(folderPath.toString()))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith("txt"))
                    .forEach(p -> {
                        executorService.execute(new FileThread(p, ngram, languageModel));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // executor no longer accepts new threads
        executorService.shutdown();

        //wait for threads to finish and then put the model into the map
            try {
                if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    allModels.put(langCode, languageModel);
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
    }
}
