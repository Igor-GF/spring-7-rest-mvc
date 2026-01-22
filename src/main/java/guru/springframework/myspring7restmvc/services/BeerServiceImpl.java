package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        beerMap = new HashMap<>();
    }

    @Override
    public List<BeerDTO> listBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: {}", id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .build();

        beerMap.put(savedBeerDTO.getId(), savedBeerDTO);
        return savedBeerDTO;
    }

    @Override
    public Optional<BeerDTO> updateById(UUID id, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(id);
        existing.setBeerName(beerDTO.getBeerName());
        existing.setUpc(beerDTO.getUpc());
        existing.setPrice(beerDTO.getPrice());
        existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteByID(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public Optional<BeerDTO> patchById(UUID beerID, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(beerID);

        if (StringUtils.hasText(beerDTO.getBeerName())) {
            existing.setBeerName(beerDTO.getBeerName());
        }

        if (beerDTO.getBeerStyle() != null) {
            existing.setBeerStyle(beerDTO.getBeerStyle());
        }

        if (beerDTO.getPrice() != null) {
            existing.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if (StringUtils.hasText(beerDTO.getUpc())) {
            existing.setUpc(beerDTO.getUpc());
        }

        return Optional.of(existing);
    }
}