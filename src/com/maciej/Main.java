package com.maciej;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String SMALL_FILE = "LIST_OF_WORDS";
    private static final String BIG_FILE = "BIG_FILE";
    private static final String UTF_8 = "UTF-8";
    private static final String COMA = ",";
    private static final String WHITESPACE = " ";

    public static void main(String[] args) {

        final Map<String, Integer> wordCountMap = initWordMapFromSmallFile();
        System.out.println(wordCountMap);

        // READING BIG FILE
        // 1. Create Path to the file using Paths factory class.
        Path bigFile = Paths.get(BIG_FILE);

        // 2. Read file at the Path location.
        try (BufferedReader reader = Files.newBufferedReader(bigFile, Charset.forName(UTF_8))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                countWords(currentLine, wordCountMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(wordCountMap);

        // WRITE TO FILE
        writeWordMapToFile(wordCountMap);
    }

    private static Map<String, Integer> initWordMapFromSmallFile() {

        final Map<String, Integer> wordCountMap = new HashMap<>();

        // READING SMALL FILE
        // 1. Create Path to the file using Paths factory class.
        Path smallFile = Paths.get(SMALL_FILE);

        // 2. Read file at the Path location - readAllLines() method first commits the contents of the file to memory (OutOfMemoryError possible)
        try {
            List<String> list_of_words = Files.readAllLines(smallFile);
            for (String line : list_of_words) {
                String[] words = line.split(COMA);
                Arrays.stream(words).forEach(word -> wordCountMap.put(word, 0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCountMap;
    }

    private static void countWords(final String currentLine, final Map<String, Integer> wordCountMap) {
        String[] words = currentLine.split(WHITESPACE);
        for (String word : words) {
            word = word.replaceAll("[.,?!]", "");
            Integer count = wordCountMap.get(word);
            if (count != null) {
                wordCountMap.put(word, count + 1);
            }
        }
    }

    private static void writeWordMapToFile(final Map<String, Integer> wordMap){
        // WRITE TO FILE
        // 1. Create new file Path
        Path outputFile = Paths.get("wordCount.txt");

        // 2. Use BufferedWriter to write to file - BufferedWriter is used to write text to a character or byte stream.
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, Charset.forName(UTF_8))){
            for (Map.Entry<String, Integer> wordCount : wordMap.entrySet()){
                writer.write(wordCount.getKey() + ": " + wordCount.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
