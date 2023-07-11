package com.github.oc.msspringmongodb.persistence;

import com.github.oc.msspringmongodb.config.DateTimeProviderConfig;
import com.github.oc.msspringmongodb.config.MongoConfig;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import({ MongoConfig.class, DateTimeProviderConfig.class })
public class PersonRepositoryIntTest {

    @Autowired
    PersonRepository personRepository;

    @Value("${ms.scenario}")
    int scenario;

    @AfterEach
    void afterEach() {
        personRepository.deleteAll();
    }

    @Nested
    @EnabledIf(expression = "#{'${ms.scenario}' == '1'}", loadContext = true)
    class Scenario1 {

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void saveWithFirstNameLastNameShouldSucceedOnJava8() {
            savePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithFirstNameLastNameShouldSucceedOnJava17() {
            savePersonTest();
        }
    }

    @Nested
    @EnabledIf(expression = "#{'${ms.scenario}' == '2'}", loadContext = true)
    class Scenario2 {

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void saveWithFirstNameLastNameArchivedDateShouldSucceedOnJava8() {
            savePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithFirstNameLastNameArchivedDateShouldFailOnJava17() {
            AssertionFailedError thrown = assertThrows(AssertionFailedError.class, PersonRepositoryIntTest.this::savePersonTest);
            System.err.println(thrown.getMessage());
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithFirstNameLastNameArchivedDateShouldSucceedOnJava17ByIgnoring() {
            savePersonTest("archivedDate");
        }
    }

    @Nested
    @EnabledIf(expression = "#{'${ms.scenario}' == '3'}", loadContext = true)
    class Scenario3 {

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void saveWithAllFieldsShouldSucceedOnJava8() {
            savePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithAllFieldsShouldFailOnJava17() {
            AssertionFailedError thrown = assertThrows(AssertionFailedError.class, PersonRepositoryIntTest.this::savePersonTest);
            System.err.println(thrown.getMessage());
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithAllFieldsShouldSucceedOnJava17ByIgnoring() {
            savePersonTest("archivedDate", "createdDate", "lastModifiedDate");
        }
    }

    @Nested
    @EnabledIf(expression = "#{'${ms.scenario}' == '4'}", loadContext = true)
    class Scenario4 {

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void updateArchivedDateShouldSucceedOnJava8() {
            updatePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void updateArchivedDateShouldFailOnJava17() {
            AssertionFailedError thrown = assertThrows(AssertionFailedError.class, PersonRepositoryIntTest.this::updatePersonTest);
            System.err.println(thrown.getMessage());
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void updateArchivedDateShouldSucceedOnJava17ByIgnoring() {
            updatePersonTest("archivedDate");
        }
    }

    @Nested
    @EnabledIf(expression = "#{'${ms.scenario}' == '5'}", loadContext = true)
    class Scenario5 {

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithFirstNameLastNameArchivedDateShouldSucceedOnJava17ByTruncating() {
            savePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void saveWithAllFieldsShouldSucceedOnJava17ByTruncating() {
            savePersonTest();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void updateArchivedDateShouldSucceedOnJava17ByTruncating() {
            updatePersonTest();
        }
    }

    private void savePersonTest(String ...ignoredFields) {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .archivedDate(determineArchivedDate())
                .build();

        personRepository.save(person);
        List<Person> persons = personRepository.findAll();

        assertThat(persons.size()).isOne();

        if (ignoredFields.length > 0) {
            assertThat(persons.get(0))
                    .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                            .withIgnoredFields(ignoredFields).build())
                    .isEqualTo(person);
        } else {
            assertThat(persons.get(0)).isEqualTo(person);
        }
    }

    private void updatePersonTest(String ...ignoredFields) {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .archivedDate(determineArchivedDate())
                .build();

        personRepository.save(person);
        Person personToUpdate = personRepository.findAll().get(0);
        personToUpdate.setArchivedDate(determineArchivedDate());
        personRepository.save(personToUpdate);
        List<Person> persons = personRepository.findAll();

        assertThat(persons.size()).isOne();

        if (ignoredFields.length > 0) {
            assertThat(persons.get(0))
                    .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                            .withIgnoredFields(ignoredFields).build())
                    .isEqualTo(personToUpdate);
        } else {
            assertThat(persons.get(0)).isEqualTo(personToUpdate);
        }
    }

    private Instant determineArchivedDate() {
        switch (scenario) {
            case 1:
            default:
                return null;

            case 2:
            case 3:
            case 4:
                return Instant.now();

            case 5:
                return Instant.now().truncatedTo(ChronoUnit.MILLIS);
        }
    }
}
