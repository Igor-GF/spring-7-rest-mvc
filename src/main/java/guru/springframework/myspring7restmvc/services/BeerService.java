package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.BeerDTO;
import guru.springframework.myspring7restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveBeer(BeerDTO beerDTO);
    Optional<BeerDTO> updateById(UUID beerId, BeerDTO beerDTO);
    Boolean deleteByID(UUID beerId);
    Optional<BeerDTO> patchById(UUID beerID, BeerDTO beerDTO);
}
