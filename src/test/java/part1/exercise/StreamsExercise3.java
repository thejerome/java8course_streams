package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> pathStream = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        );

        String collect = pathStream.flatMap(path -> {
            try {
                return Files.readAllLines(path, Charset.forName("windows-1251"))
                        .parallelStream()
                        .map(s -> s.split("[^а-яА-Яa-zA-Z]"))
                        .flatMap(Arrays::stream)
                        .map(String::toLowerCase)
                        .filter(s -> s.length() >= 4);
            } catch (IOException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        }).collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .filter(s -> s.getValue() >= 10)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(joining("\n"));

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, collect);
    }


}
