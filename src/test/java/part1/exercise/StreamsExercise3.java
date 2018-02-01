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
                .reduce(new HashMap<String, Integer>(),
                        (m, p) -> {
                            HashMap<String, Integer> m1 = m;
                            if (m1.containsKey(p.getKey())) {
                                m1.put(p.getKey(), m1.get(p.getKey()) + 1);
                            } else {
                                m1.put(p.getKey(), p.getValue());
                            }
                            return m1;
                        },
                        (m1, m2) -> {
                            m1.forEach((key, value) -> m2.merge(key, value, (v1, v2) -> v1 + v2));
                            return m2;
                        })
                .entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .sorted(compareByCount.thenComparing(compareByLetter))
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
