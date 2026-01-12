package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveBeer(BeerDTO beerDTO);
    void updateById(UUID beerId, BeerDTO beerDTO);
    void deleteByID(UUID beerId);
    void patchById(UUID beerID, BeerDTO beerDTO);
}
