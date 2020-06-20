package com.fachini.beercrudl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fachini.beercrudl.entities.Beer;
import com.fachini.beercrudl.repositories.BeerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BeerService {

    private final BeerRepository beerRepository;

    public List<Beer> findAll() {
        return beerRepository.findAll();
    }

    public Optional<Beer> findById(UUID id) {
        return beerRepository.findById(id);
    }

    public Beer save(Beer stock) {
        return beerRepository.save(stock);
    }

    public void deleteById(UUID id) {
        beerRepository.deleteById(id);
    }
}
