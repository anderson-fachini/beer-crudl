package com.fachini.beercrudl.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.fachini.beercrudl.entities.Beer;
import com.fachini.beercrudl.repositories.BeerRepository;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BeerService {

    private final BeerRepository beerRepository;

    private final PagedResourcesAssembler<Beer> pagedAssembler;

    public PagedModel<EntityModel<Beer>> findAll(Predicate predicate, Pageable pageable) {
        return pagedAssembler.toModel(beerRepository.findAll(predicate, pageable));
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
