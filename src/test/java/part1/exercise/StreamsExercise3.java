package part1.exercise;

import data.WNPResult;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        String result = pathStream
            .flatMap(path -> {
                try {
                    return Files.lines(path, Charset.forName("windows-1251"));
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            })
            .flatMap(s -> Arrays.stream(s.split("[^а-яА-Яa-zA-Z]+")))
            .filter(word -> word.length() >= 4)
            .map(String::toLowerCase)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            //.sorted(Comparator.comparing(Entry::getValue).thenComparing(Entry::getKey))
            .sorted((o1, o2) -> {
                if (o1.getValue() > o2.getValue())
                    return -1;
                else if (o1.getValue() < o2.getValue())
                    return 1;
                else {
                    return o1.getKey().compareTo(o2.getKey());
                }
            })
            .filter(entry -> entry.getValue() >= 10)
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .collect(Collectors.joining("\n"));

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
