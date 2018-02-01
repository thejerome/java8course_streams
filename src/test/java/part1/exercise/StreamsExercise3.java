package part1.exercise;

import data.WNPResult;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
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
        Map<String, Long> index = Stream.of(
            Paths.get("WAP12.txt"),
            Paths.get("WAP34.txt"))
            .map(path -> {
                try {
                    return new String(Files.readAllBytes(path), Charset.forName("Windows-1251"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(s -> Arrays.asList(s.split("[^а-яА-Яa-zA-Z]")))
            .flatMap(Collection::stream)
            .map(String::toLowerCase)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        String result = index.entrySet()
            .stream()
            .filter(entry -> entry.getKey().length() >= 4)
            .filter(entry -> entry.getValue() >= 10)
            .sorted((entry1, entry2) -> entry1.getValue().equals(entry2.getValue())
                ? entry1.getKey().compareTo(entry2.getKey())
                : entry2.getValue().compareTo(entry1.getValue()))
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .collect(Collectors.joining("\n"));
        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
