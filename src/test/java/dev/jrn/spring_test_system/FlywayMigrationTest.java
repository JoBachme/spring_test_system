package dev.jrn.spring_test_system;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.jrn.spring_test_system.repository.StudentRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:flyway_migration_test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.sql.init.mode=never",
        "spring.flyway.enabled=true",
        "spring.flyway.baseline-on-migrate=false"
})
class FlywayMigrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private StudentTestRepository studentTestRepository;

    @Test
    void flywayMigrations_ShouldCreateSchemaAndSampleData() {
        assertThat(studentRepository.count()).isEqualTo(7);
        assertThat(testRepository.count()).isEqualTo(8);
        assertThat(studentTestRepository.count()).isEqualTo(37);
    }
}
