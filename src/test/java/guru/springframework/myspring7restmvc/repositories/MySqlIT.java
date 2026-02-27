package guru.springframework.myspring7restmvc.repositories;

import guru.springframework.myspring7restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySqlIT {

    @Container
    @ServiceConnection // Overwrites automatically the properties with the container ones
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:9.2");

//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
//        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
//        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
//    }

    // datasource was brought in to demonstrate how the dynamic properties work
    @Autowired
    DataSource dataSource;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void test_list_beer() {
        List<Beer> allBeers = beerRepository.findAll();
        assertThat(allBeers.size()).isGreaterThan(0);
    }

}
