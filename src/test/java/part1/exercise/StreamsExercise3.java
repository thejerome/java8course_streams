package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {

        String result = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        ).flatMap(f -> {
            try {
                return Files.lines(f, Charset.forName("windows-1251"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).flatMap(s -> Arrays.stream(s.split("[^а-яА-Яa-zA-Z]+")))
                .filter(s -> s.length() >= 4)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .sorted((e1, e2) -> e1.getValue() > e2.getValue() ? -1
                        : e1.getValue().equals(e2.getValue()) ? e1.getKey().compareTo(e2.getKey())
                        : 1)
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
