import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class NgramAnalyzer {
    public static void main(String[] args) {
        String directoryPath = "Books"; 
        String outputFilePath = "output.txt"; 

        try {
            Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile) // Take files
                .forEach(filePath -> processFile(filePath.toString(), outputFilePath)); // Call the processFile method for each file 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processFile(String filePath, String outputFilePath) {
        long startTime = System.nanoTime(); // Take start time

        try {
            // Read file
            String text = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            String[] words = text.split("\\s+");

            // HashMaps
            Map<String, Integer> oneGramFreq = new HashMap<>();
            Map<String, Integer> twoGramFreq = new HashMap<>();
            Map<String, Integer> threeGramFreq = new HashMap<>();

            // Count 1-gram 
            for (String word : words) {
                word = word.toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                if (word.isEmpty()) continue;
                oneGramFreq.put(word, oneGramFreq.getOrDefault(word, 0) + 1);
            }

            // Count 2-gram
            for (int i = 0; i < words.length - 1; i++) {
                String word1 = words[i].toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                String word2 = words[i + 1].toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                if (word1.isEmpty() || word2.isEmpty()) continue;
                String twoGram = word1 + " " + word2;
                twoGramFreq.put(twoGram, twoGramFreq.getOrDefault(twoGram, 0) + 1);
            }

            // Count 3-gram
            for (int i = 0; i < words.length - 2; i++) {
                String word1 = words[i].toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                String word2 = words[i + 1].toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                String word3 = words[i + 2].toLowerCase().replaceAll("[^a-zA-ZğüşöçıİĞÜŞÖÇ]", "");
                if (word1.isEmpty() || word2.isEmpty() || word3.isEmpty()) continue;
                String threeGram = word1 + " " + word2 + " " + word3;
                threeGramFreq.put(threeGram, threeGramFreq.getOrDefault(threeGram, 0) + 1);
            }

            // Write results
            StringBuilder result = new StringBuilder();
            result.append("\n--- File: " + filePath + " ---\n");

            result.append("\n--- 1-gram ---\n");
            oneGramFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEach(entry -> result.append(entry.getKey() + " : " + entry.getValue() + "\n"));

            result.append("\n--- 2-gram ---\n");
            twoGramFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEach(entry -> result.append(entry.getKey() + " : " + entry.getValue() + "\n"));

            result.append("\n--- 3-gram ---\n");
            threeGramFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEach(entry -> result.append(entry.getKey() + " : " + entry.getValue() + "\n"));

            // Count time
            long endTime = System.nanoTime();
            long duration = endTime - startTime; // Process time
            double seconds = (double) duration / 1_000_000_000.0; // Convert to seconds

            result.append("\n--- Processing Time ---\n");
            result.append("Time: " + seconds + " seconds\n");

            // Write results to output.txt
            Files.writeString(Paths.get(outputFilePath), result.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
