package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
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

        Map<String, Long> collection = pathStream.flatMap(path -> {
            try {
                return Files.readAllLines(path, Charset.forName("windows-1251"))
                        .stream()
                        .map(s -> s.split("\\P{L}+"))
                        .flatMap(Arrays::stream)
                        .map(String::toLowerCase)
                        .filter(s -> s.length() > 3);
            } catch (IOException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String result = collection
                .entrySet().stream()
                .filter(string -> string.getValue() > 9)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
