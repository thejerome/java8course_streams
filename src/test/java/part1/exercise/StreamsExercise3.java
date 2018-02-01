package part1.exercise;

import data.WNPResult;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> streamWar = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
                );

        Comparator<Map.Entry<String,Integer>> compareByCount = (e1, e2) -> Integer.compare(e2.getValue(), e1.getValue());
        Comparator<Map.Entry<String,Integer>> compareByLetter = Comparator.comparing(Map.Entry::getKey);

        String result = streamWar
                .flatMap(p -> {
                    try {
                        return Files.lines(p, Charset.forName("windows-1251"));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Stream.empty();
                    }
                })
                .flatMap(s -> Arrays.stream(s.split("[^a-zA-Zа-яА-Я]+")))
                .map(String::toLowerCase)
                .filter(s -> s.length() >= 4)
                .map(s -> new Pair<>(s, 1))
                .collect(toMap(Pair::getKey,Pair::getValue, (v1,v2) -> v1 + v2))
                .entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .sorted(compareByCount.thenComparing(compareByLetter))
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
