package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();
    Beer getBeerById(UUID id);
    Beer saveBeer(Beer beer);
    Beer updateById(UUID beerId, Beer beer);
    void deleteByID(UUID beerId);
    void patchById(UUID beerID, Beer beer);
}
