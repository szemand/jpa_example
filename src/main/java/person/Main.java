package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.Locale;

public class Main {

    private static Faker faker = new Faker(new Locale("en"));

    private static Person randomPerson() {
        Person person = Person.builder()
                .name(faker.name().name())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.class))
                .address(Address.builder()
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .city(faker.address().city())
                        .streetAddress(faker.address().streetAddress())
                        .zip(faker.address().zipCode())
                        .build())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();
        return person;

    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
        EntityManager em = emf.createEntityManager();

        for(int i = 0; i < 1000; i++){
            em.getTransaction().begin();
            Person person = randomPerson();
            em.persist(person);
            em.getTransaction().commit();
        }

        em.createQuery("SELECT l FROM Person l", Person.class).getResultList().stream().forEach(System.out::println);
        em.close();
        emf.close();
    }
}

