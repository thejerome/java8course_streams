package part1.exercise;

import com.google.common.io.Files;
import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        String result = null; Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        )
                .flatMap(p -> {
                    try {
                        return Files.readLines(p.toFile(), Charset.forName("windows-1251")).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }})
                .map(line -> line.split("[^а-яА-Яa-zA-Z]"))
                .flatMap(Arrays::stream);


        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
