package com.github.oc.msspringmongodb.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PersonRepositoryIntTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void save() {
        Person person = new Person();
        personRepository.save(person);

        List<Person> persons = personRepository.findAll();

        assertThat(persons.size()).isOne();
        assertThat(persons.get(0)).isEqualTo(person);
    }
}
