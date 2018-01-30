package part1.exercise;

import data.WNPResult;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        String result = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt"))
            .flatMap(p -> {
            try {
                return Files.lines(p, Charset.forName("windows-1251"));
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }).flatMap(s -> Arrays.stream(s.split("[^а-яА-Яa-zA-Z]+")))
            .filter(s -> s.length() >= 4)
            .map(String::toLowerCase)
            .collect(Collectors
                .groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .filter(e -> e.getValue() > 9)
            .sorted((i1, i2) -> i1.getValue() > i2.getValue()
                ? -1 : i1.getValue().equals(i2.getValue())
                ? i1.getKey().compareTo(i2.getKey()) : 1)
            .map(e -> e.getKey() + " - " + e.getValue())
            .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
