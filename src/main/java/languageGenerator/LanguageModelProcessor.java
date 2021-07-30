package languageGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class LanguageModelProcessor {

    public static synchronized Map<String, Long> getWordHistogram(String word, int ngram) {
        return ngram >= word.length() ? Collections.singletonMap((word.toLowerCase()), 1L) :
                        IntStream.range(0, word.length() - ngram + 1)
                                .mapToObj(i -> word.substring(i, i + ngram).toLowerCase())
                                .collect(Collectors.groupingBy(
                                        Function.identity(),
                                        Collectors.counting()));
    }

    public static synchronized Map<String, Long> getLanguageModel(Path filePath, int ngram) {
        Map<String, Long> fileLanguageModel = new HashMap<>();

        try (Stream<String> readFiles = Files.lines(filePath)){

                Pattern pattern = Pattern.compile("\\s+");

                List<String> listString = readFiles
                        .map(line -> line.replaceAll("(?!\\\\')\\p{P}", ""))
                        .flatMap(line -> pattern.splitAsStream(line))
                        .collect(toList());

                List<Map<String, Long>> wordHistogramList = listString.stream()
                        .map(x -> getWordHistogram(x, ngram))
                        .collect(toList());

                fileLanguageModel = wordHistogramList.stream()
                        .filter(x -> !x.containsKey(""))
                        .flatMap(x -> x.entrySet().stream())
                        .collect(groupingBy(Map.Entry::getKey, summingLong(Map.Entry::getValue)));
            }
        catch (IOException e) {
            e.printStackTrace();
        }

        return fileLanguageModel;
    }


    public static synchronized Map<String, Long> getLanguageModelMystery(String text, int ngram) throws IOException {

        Map<String, Long> fileLanguageModel = new HashMap<>();

            Pattern pattern = Pattern.compile("\\s+");

        List<String> listString = text.lines()
                .map(line -> line.replaceAll("(?!\\\\')\\p{P}", ""))
                .flatMap(line -> pattern.splitAsStream(line))
                .collect(toList());

        List<Map<String, Long>> wordHistogramList = listString.stream()
                .map(x -> getWordHistogram(x, ngram))
                .collect(toList());

        fileLanguageModel = wordHistogramList.stream()
                .filter(x -> !x.containsKey(""))
                .flatMap(x -> x.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingLong(Map.Entry::getValue)));


        return fileLanguageModel;
    }


    public static String findSmallestLanguageDistance(Map<String, Double> languageDistanceMaps) {
        return languageDistanceMaps.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .get();
    }

    public static void calcDocumentDistance(String languageName, Map<String, Long> languageModel,
                                            Map<String, Long> mysteryTextModel, Map<String, Double> languageDistanceMap)
    {
        double productAB = languageModel.entrySet().stream()
                .filter(entry -> mysteryTextModel.containsKey(entry.getKey()))
                .mapToDouble(entry -> entry.getValue() * mysteryTextModel.get(entry.getKey())).sum();

        double languageSquaredVector = languageModel.entrySet().stream()
                .mapToDouble(entry -> entry.getValue()*entry.getValue())
                .sum();

        double mysterySquaredVector = mysteryTextModel.entrySet().stream()
                .mapToDouble(entry -> entry.getValue()*entry.getValue())
                .sum();

        double languageVector = Math.sqrt(languageSquaredVector);
        double mysteryVector = Math.sqrt(mysterySquaredVector);

        double similarity = productAB/(languageVector*mysteryVector);

        languageDistanceMap.put(languageName, similarity);
    }


    }