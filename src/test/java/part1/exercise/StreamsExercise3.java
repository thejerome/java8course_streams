package part1.exercise;

import static java.nio.file.Files.readAllLines;
import static org.junit.Assert.assertEquals;

import data.WNPResult;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> stream = Stream.of(
            Paths.get("WAP12.txt"),
            Paths.get("WAP34.txt")
        );

        String result = stream
            .flatMap(path -> {
                try {
                    return readAllLines(path, Charset.forName("windows-1251")).stream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Stream.empty();
                }
            })
            .flatMap(line -> Arrays.stream(line.split("[^а-яА-Яa-zA-Z]")))
            .filter(word -> word.length() >= 4)
            .map(String::toLowerCase)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() >= 10)
            .sorted((entry1, entry2) ->
                entry1.getValue().equals(entry2.getValue()) ?
                    entry1.getKey().compareTo(entry2.getKey()) : Long.compare(entry2.getValue(), entry1.getValue()))
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
