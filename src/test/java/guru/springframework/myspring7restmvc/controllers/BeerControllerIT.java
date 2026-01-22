package guru.springframework.myspring7restmvc.controllers;

import guru.springframework.myspring7restmvc.entities.Beer;
import guru.springframework.myspring7restmvc.mappers.BeerMapper;
import guru.springframework.myspring7restmvc.model.BeerDTO;
import guru.springframework.myspring7restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void test_delete_beer_returns_not_found() {
        assertThrows(NotFoundException.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void test_delete_beer() {
        Beer beer = beerRepository.findAll().getFirst();

        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void test_update_returns_not_found() {
        assertThrows(NotFoundException.class, () -> {
            beerController.handleUpdateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void test_update_beer() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDto = beerMapper.beerToBeerDTO(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName("Updated beer");

        ResponseEntity responseEntity = beerController.handleUpdateBeerById(beer.getId(), beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerDto.getBeerName());
    }

    @Test
    void test_list_beers() {
        List<BeerDTO> dtos = beerController.getBeers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void test_list_beers_return_empty() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.getBeers();

        assertThat(dtos.size()).isEqualTo(0);

    }

    @Test
    void test_get_beer_by_id() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void test_get_beer_by_id_returns_not_found() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });

    }

    @Test
    void test_save_new_beer() {
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = beerController.handlePost(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] split = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUuid = UUID.fromString(split[split.length - 1]);

        Beer beer = beerRepository.findById(savedUuid).get();
        assertThat(beer).isNotNull();
    }
}