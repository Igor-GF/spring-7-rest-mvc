package guru.springframework.myspring7restmvc.controllers;

import guru.springframework.myspring7restmvc.model.Beer;
import guru.springframework.myspring7restmvc.services.BeerService;
import guru.springframework.myspring7restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Spring boot brings here a object mapper configured with sensible default
    // To be further used to produce JSON
    @Autowired
    ObjectMapper objectMapper;

    // Required dependency for BeerController
    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    ArgumentCaptor<Beer> beerCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void test_get_all_beers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void test_get_beer_by_id() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        mockMvc.perform(get(BeerController.BEER_PATH + "/" + testBeer.getId())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void test_create_new_beer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().getFirst();
        beer.setId(null);
        beer.setVersion(null);

        given(beerService.saveBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                    .accept(MediaType.APPLICATION_JSON) // expects response JSON
                    .contentType(MediaType.APPLICATION_JSON) // expected body type JSON
                    .content(objectMapper.writeValueAsString(beer))) // given body JSON: model converted by the Jackson library
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void test_update_beer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        mockMvc.perform(put(BeerController.BEER_PATH + "/" + testBeer.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateById(uuidCaptor.capture(), any(Beer.class));

        assertThat(testBeer.getId()).isEqualTo(uuidCaptor.getValue());
    }

    @Test
    void test_delete_beer_by_id() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        mockMvc.perform(delete(BeerController.BEER_PATH + "/" + testBeer.getId())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteByID(uuidCaptor.capture());

        assertThat(testBeer.getId()).isEqualTo(uuidCaptor.getValue());
    }

    @Test
    void test_patch_beer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        // trick to create a JSON object
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Test-beer-name");

        mockMvc.perform(patch(BeerController.BEER_PATH + "/" + testBeer.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidCaptor.capture(), beerCaptor.capture());

        assertThat(testBeer.getId()).isEqualTo(uuidCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerCaptor.getValue().getBeerName());

    }
}