package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
                );


        String result = null;
        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10
        result = Stream.of(Paths.get("WAP12.txt"), Paths.get("WAP34.txt"))
                .flatMap(path -> {
                    try {
                        return Files.lines(path, Charset.forName("windows-1251"));
                    } catch (IOException e) {
                        throw new RuntimeException("error while reading from files", e);
                    }
                })
                .map(str -> str.split("[^а-яА-ЯёЁa-zA-Z]"))
                .flatMap(arr -> Arrays.stream(arr))
                .map(String::toLowerCase)
                .filter(word -> word.length() >= 4)
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                word -> 1,
                                (oldCount, newCount) -> ++oldCount,
                                HashMap::new
                        )
                )
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= 10)
                .sorted(
                        (o1, o2) -> {
                            int compareResult = o2.getValue().compareTo(o1.getValue());
                            if (compareResult == 0) {
                                return o1.getKey().compareTo(o2.getKey());
                            } else {
                                return compareResult;
                            }
                        }
                )
                .map(entry -> entry.getKey() + " - " + entry.getValue() )
                .collect(Collectors.joining("\n"));

        assertEquals(new WNPResult().result, result);
    }

}
