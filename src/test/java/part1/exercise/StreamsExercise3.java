package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        final Stream<Path> wap = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
                );


        String result = wap.flatMap(p -> {
                    try {
                        return Files.readAllLines(p, Charset.forName("windows-1251"))
                                .parallelStream()
                                .map(s -> s.split("[^а-яА-Яa-zA-Z]"))
                                .flatMap(Arrays::stream)
                                .filter(s -> s.length() >= 4)
                                .map(String::toLowerCase);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(stringLongEntry -> stringLongEntry.getValue() >= 10)
                .sorted(Map.Entry
                        .<String, Long> comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(stringLongEntry -> stringLongEntry.getKey() + " - " + stringLongEntry.getValue())
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
