package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();
    Optional<Beer> getBeerById(UUID id);
    Beer saveBeer(Beer beer);
    void updateById(UUID beerId, Beer beer);
    void deleteByID(UUID beerId);
    void patchById(UUID beerID, Beer beer);
}
