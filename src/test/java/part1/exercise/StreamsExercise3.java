package part1.exercise;

import static org.junit.Assert.assertEquals;

import data.WNPResult;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
        Stream<Path> pathStream = Stream.of(
            Paths.get("WAP12.txt"),
            Paths.get("WAP34.txt")
        );



        String result = pathStream.flatMap(path -> {
            try {
                return Files.readAllLines(path, Charset.forName("windows-1251")).stream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Stream.empty();
        })
            .parallel()
            .flatMap(s -> Arrays.stream(s.split("\\P{L}+")))
            .filter(s -> s.length() >= 4)
            .map(String::toLowerCase)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .filter(s -> s.getValue() >= 10)
            .sorted(((o1, o2) -> {
                long res = - Long.compare(o1.getValue(), o2.getValue());
                res = res == 0? o1.getKey().compareTo(o2.getKey()): res;
                return (int)res;
                }))
            .map(e -> e.getKey() + " - " + e.getValue())
            .collect(Collectors.joining("\n"));
        System.out.println(result);

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
