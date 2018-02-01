package part1.exercise;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import javafx.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class StreamsExercise2 {
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    static class PersonEmployerPair{
        private final Person person;
        private final String employer;

        public PersonEmployerPair(Person person, String employer) {
            this.person = person;
            this.employer = employer;
        }

        public Person getPerson() {
            return person;
        }

        public String getEmployer() {
            return employer;
        }
    }

    private static Stream<PersonEmployerPair> employeeToPairs(Employee employee) {
        return employee.getJobHistory().stream()
                .map(JobHistoryEntry::getEmployer)
                .map(employer -> new PersonEmployerPair(employee.getPerson(), employer));
    }

    @Test
    public void employersStuffLists() {
        final List<Employee> employees = getEmployees();

        final Stream<PersonEmployerPair> personEmployerPairStreamStream = employees.stream()
                .flatMap(StreamsExercise2::employeeToPairs);

        Map<String, List<Person>> employersStuffLists = personEmployerPairStreamStream
                .collect(groupingBy(
                        PersonEmployerPair::getEmployer,
                        mapping(PersonEmployerPair::getPerson, toList())));

        assertEquals(getExpectedEmployersStuffLists(), employersStuffLists);
    }

    @Test
    public void indexByFirstEmployer() {
        final List<Employee> employees = getEmployees();

        Map<String, List<Person>> employeesIndex = employees.stream()
                .map(e -> new Pair<>(e.getJobHistory().get(0).getEmployer(), e.getPerson()))
                        .collect(groupingBy(
                                Pair::getKey,
                                mapping(Pair::getValue, toList())));

        assertEquals(getExpectedEmployeesIndexByFirstEmployer(), employeesIndex);

    }

    private static class PersonPositionDuration {
        private final Person person;
        private final String employer;
        private final int duration;

        public PersonPositionDuration(Person person, String employer, int duration) {
            this.person = person;
            this.employer = employer;
            this.duration = duration;
        }

        public Person getPerson() {
            return person;
        }

        public String getEmployer() {
            return employer;
        }

        public int getDuration() {
            return duration;
        }

    }

    @Test
    public void greatestExperiencePerEmployer() {
        Map<String, Person> employeesIndex = getEmployees().stream()
                .flatMap(e -> e.getJobHistory().stream()
                .map(j -> new PersonPositionDuration(e.getPerson(), j.getEmployer(), j.getDuration())))
                .collect(groupingBy(
                        PersonPositionDuration::getEmployer,
                        collectingAndThen(
                                maxBy(comparing(PersonPositionDuration::getDuration)), p -> p.get().getPerson())));

        assertEquals(new Person("John", "White", 28), employeesIndex.get("epam"));
    }


    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 21),
                        Arrays.asList(
                                new JobHistoryEntry(4, "BA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 22),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 23),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 24),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "BA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 25),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 26),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Bob", "Doe", 27),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 28),
                        Collections.singletonList(
                                new JobHistoryEntry(666, "BA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 29),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 30),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(5, "dev", "abc")
                        )),
                new Employee(
                        new Person("Bob", "White", 31),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        ))
        );
    }

    private static Map<String, List<Person>> getExpectedEmployersStuffLists() {
        Map<String, List<Person>> map = ImmutableMap.of(
                "abc", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "yandex", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "epam", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "Doe", 21),
                        new Person("John", "White", 22),
                        new Person("John", "Galt", 23),
                        new Person("John", "Doe", 24),
                        new Person("John", "White", 25),
                        new Person("John", "Galt", 26),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "White", 28),
                        new Person("John", "Galt", 29),
                        new Person("John", "Doe", 30),
                        new Person("Bob", "White", 31)
                ),
                "google", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "Galt", 23),
                        new Person("John", "Galt", 26),
                        new Person("John", "Galt", 29)
                )
        );

        return map;
    }

    private static Map<String, List<Person>> getExpectedEmployeesIndexByFirstEmployer() {
        Map<String, List<Person>> map = ImmutableMap.of(
                "yandex", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "epam", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "White", 22),
                        new Person("John", "Galt", 23),
                        new Person("John", "White", 25),
                        new Person("John", "Galt", 26),
                        new Person("John", "White", 28),
                        new Person("John", "Galt", 29),
                        new Person("Bob", "White", 31)
                )
        );

        return map;
    }

}
