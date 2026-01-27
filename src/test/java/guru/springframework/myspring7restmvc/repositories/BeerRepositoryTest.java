package guru.springframework.myspring7restmvc.repositories;

import guru.springframework.myspring7restmvc.entities.Beer;
import guru.springframework.myspring7restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void test_save_beer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                            .beerName("My beer")
                            .beerStyle(BeerStyle.PALE_ALE)
                            .upc("321321321")
                            .price(new BigDecimal("11.99"))
                        .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void test_save_beer_too_long_beerName() {

        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("My beer 12345678901234567890123456789012345678901234567890")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("321321321")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }
}