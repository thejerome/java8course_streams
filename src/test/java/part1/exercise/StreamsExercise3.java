package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        String result =
                Stream.of(
                        Paths.get("WAP12.txt"),
                        Paths.get("WAP34.txt")
                )
                        .flatMap(path -> {
                            try {
                                return Files.lines(path, Charset.forName("windows-1251"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .map(string -> string.split(" "))
                        .flatMap(Arrays::stream)
                        .map(word -> word.toLowerCase().trim().replaceAll("[^A-Za-zа-яА-Я\\-]+", " ").split(" "))
                        .flatMap(Arrays::stream)
                        .filter(word -> word.length() > 3)
                        .map(word -> new StringIntPair(word, 1))
                        .collect(Collectors.toMap(StringIntPair::getWord, StringIntPair::getCount, (prev, cur) -> prev + cur))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue() > 9)
                        .sorted((o1, o2) -> {
                            int compare = o2.getValue().compareTo(o1.getValue());
                            if (compare == 0) {
                                return o1.getKey().compareTo(o2.getKey());
                            } else
                                return compare;
                        })
                        .map(entry -> entry.getKey() + " - " + entry.getValue())
                        .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

    private class StringIntPair {
        private final String word;
        private int count;

        StringIntPair(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        String getWord() {
            return word;
        }
        int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
