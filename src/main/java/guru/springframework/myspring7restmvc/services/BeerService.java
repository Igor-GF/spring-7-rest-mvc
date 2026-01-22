package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveBeer(BeerDTO beerDTO);
    Optional<BeerDTO> updateById(UUID beerId, BeerDTO beerDTO);
    Boolean deleteByID(UUID beerId);
    Optional<BeerDTO> patchById(UUID beerID, BeerDTO beerDTO);
}
