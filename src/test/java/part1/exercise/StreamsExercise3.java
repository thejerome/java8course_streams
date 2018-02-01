package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> wap = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        );

        String result = wap.flatMap(path -> {
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
                .filter(word -> word.length() >= 4)
                .map(word -> new WordCount(word, 1))
                .collect(Collectors.toMap(wordCount -> wordCount.getWord(), wordCount -> wordCount.getCount(), (prev, cur) -> prev + cur))
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= 10)
                .sorted((o1, o2) -> {
                    int compare = o2.getValue().compareTo(o1.getValue());
                    if (compare == 0) {
                        return o1.getKey().compareTo(o2.getKey());
                    } else
                        return compare;
                })
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));
        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }
    private class WordCount {
        private final String word;
        private int count;

        WordCount(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        String getWord() {
            return word;
        }
        int getCount() {
            return count;
        }
    }
}
