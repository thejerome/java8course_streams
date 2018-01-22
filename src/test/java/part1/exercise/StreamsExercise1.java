package part1.exercise;

import data.Employee;
import data.JobHistoryEntry;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static data.Generator.generateEmployeeList;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StreamsExercise1 {
    private static final List<Employee> EMPLOYEES;

    static {
        EMPLOYEES = generateEmployeeList();
    }
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    @Test
    public void getAllEpamEmployees() {
        //We proceed from what needs to be done according to the tests given and the syntax of
        // the method (Employees, that have worked only for EPAM)
        Predicate<Employee> withEpamExperience = ((e) -> e.getJobHistory().stream().allMatch(
                j -> j.getEmployer().equals("epam")));

        List<Employee> epamEmployees = EMPLOYEES.stream()
                .filter(withEpamExperience)
                .collect(Collectors.toList());

        assertFalse(epamEmployees.toString().contains("employer=google"));
        assertFalse(epamEmployees.toString().contains("employer=yandex"));
        assertFalse(epamEmployees.toString().contains("employer=abc"));
    }

    @Test
    public void getEmployeesStartedFromEpam() {
        //We proceed from what needs to be done according to the tests given and the syntax (name)
        // of the method (Employees started from EPAM)
        Predicate<Employee> epamExperienceFirst = ((h) -> h.getJobHistory().stream()
                .findFirst()
                .filter(e -> e.getEmployer().equals("epam")).isPresent());

        List<Employee> epamEmployees = EMPLOYEES.stream()
                .filter(epamExperienceFirst)
                .collect(Collectors.toList());

        assertNotNull(epamEmployees);
        assertFalse(epamEmployees.isEmpty());

        for (Employee epamEmployee : epamEmployees) {
            assertEquals("epam", epamEmployee.getJobHistory().get(0).getEmployer());
        }
    }

    @Test
    public void sumEpamDurations() {
        final List<Employee> employees = generateEmployeeList();

        Integer expected = 0;

        for (Employee e : employees) {
            for (JobHistoryEntry j : e.getJobHistory()) {
                if (j.getEmployer().equals("epam")) {
                    expected += j.getDuration();
                }
            }
        }

        Integer result = employees.stream()
                .flatMap(e -> e.getJobHistory().stream())
                .filter((e) -> e.getEmployer().equals("epam"))
                .mapToInt(JobHistoryEntry::getDuration)
                .sum();
        assertEquals(expected, result);
    }

}
