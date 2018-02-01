package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
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
        )
                .flatMap(p -> {
                    try {
                        return Files.lines(p, Charset.forName("windows-1251"));
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                })
                .parallel()
                .map(String::toLowerCase)
                .map(l -> l.split("[^\\p{javaLowerCase}]+"))
                .flatMap(a -> Stream.of(a).parallel())
                .filter(w -> w.length() >= 4)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().parallel()
                .filter(e -> e.getValue() >= 10)
                .sorted(Comparator.comparingLong((ToLongFunction<Map.Entry<String, Long>>) Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Comparator.comparing(Map.Entry::getKey)))
                .map(e -> e.getKey().concat(" - ").concat(e.getValue().toString()))
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
