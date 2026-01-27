package guru.springframework.myspring7restmvc.controllers;

import guru.springframework.myspring7restmvc.entities.Beer;
import guru.springframework.myspring7restmvc.mappers.BeerMapper;
import guru.springframework.myspring7restmvc.model.BeerDTO;
import guru.springframework.myspring7restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void test_patch_beer_bad_name() throws Exception {
        Beer testBeer = beerRepository.findAll().getFirst();

        // trick to create a JSON object
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Test-beer-name-really-long-1234567890123456789012345678901234567890");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

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
            beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void test_update_beer() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDto = beerMapper.beerToBeerDTO(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName("Updated beer");

        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDto);

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

    @Rollback
    @Transactional
    @Test
    void test_save_new_beer() {
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = beerController.saveBeer(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] split = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUuid = UUID.fromString(split[split.length - 1]);

        Beer beer = beerRepository.findById(savedUuid).get();
        assertThat(beer).isNotNull();
    }
}