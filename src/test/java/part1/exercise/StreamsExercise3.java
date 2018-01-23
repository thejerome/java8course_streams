package part1.exercise;

import data.WNPResult;
import javafx.util.Pair;
import org.junit.Test;

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
        Stream<Path> pathStream = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        );

        String result = pathStream
                .flatMap(path -> {
                    try {
                        return Files.lines(path, Charset.forName("windows-1251"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(line -> line.toLowerCase().trim().replaceAll("[^A-Za-zа-яА-Я]+", " "))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .filter(s -> s.length() >= 4)
                .map(s -> new Pair<String, Integer>(s, 1))
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                Pair::getKey,
                                Collectors.counting()
                        ),
                        map -> map.entrySet().stream()
                                .filter(entry -> entry.getValue() >= 10)
                                .sorted((e1, e2) -> {
                                    int rez = e2.getValue().compareTo(e1.getValue());
                                    return rez != 0 ? rez : e1.getKey().compareTo(e2.getKey());

                                })
                                .map(entry -> entry.getKey() + " - " + entry.getValue())
                                .collect(Collectors.joining("\n"))
                ));
        assertEquals(new WNPResult().result, result);
    }

}
