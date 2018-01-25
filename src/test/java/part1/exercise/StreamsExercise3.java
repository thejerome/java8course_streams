package part1.exercise;

import data.WNPResult;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
        Stream<Path> pathStream = Stream.of(
            Paths.get("WAP12.txt"),
            Paths.get("WAP34.txt")
        );

        String result = pathStream.flatMap(p -> {
                    try {
                        return Files.readAllLines(p, Charset.forName("windows-1251")).stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
            ).flatMap(s -> Arrays.stream(s.split("\\P{L}+")))
                .map(s -> s.toLowerCase())
                .filter(s -> s.length() >= 4)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(s -> s.getValue() >= 10)
                .sorted((a, b) -> {
                    int res = -Long.compare(a.getValue(), b.getValue());
                    int i = res == 0 ? a.getKey().compareTo(b.getKey()) : res;
                    return i;
                })
                .map(s -> s.getKey() + " - " + s.getValue())
                .collect(Collectors.joining("\n"));

        System.out.println(result);

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
