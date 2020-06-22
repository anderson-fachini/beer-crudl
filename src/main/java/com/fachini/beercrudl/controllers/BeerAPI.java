package com.fachini.beercrudl.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fachini.beercrudl.entities.Beer;
import com.fachini.beercrudl.repositories.BeerRepository;
import com.fachini.beercrudl.services.BeerService;
import com.fachini.beercrudl.services.FileStorageService;
import com.querydsl.core.types.Predicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Beer")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerAPI {

    private final BeerService beerService;

    private final FileStorageService fileStorageService;

    @ApiOperation("List all registered beers")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Beer>>> findAll(@QuerydslPredicate(root = Beer.class, bindings = BeerRepository.class) Predicate predicate, Pageable pageable) {
        return ResponseEntity.ok(beerService.findAll(predicate, pageable));
    }

    @ApiOperation("Create a beer")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Beer beer) {
        return ResponseEntity.ok(beerService.save(beer));
    }

    @ApiOperation("Get a specific beer")
    @GetMapping("/{id}")
    public ResponseEntity<Beer> findById(@PathVariable UUID id) {
        Optional<Beer> optBeer = beerService.findById(id);
        if (!optBeer.isPresent()) {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(optBeer.get());
    }

    @ApiOperation("Update a beer")
    @PutMapping("/{id}")
    public ResponseEntity<Beer> update(@PathVariable UUID id, @Valid @RequestBody Beer beer) {
        if (!beerService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(beerService.save(beer));
    }

    @ApiOperation("Delete a beer")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (!beerService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }

        beerService.deleteById(id);
        fileStorageService.deleteFile(id);

        return ResponseEntity.ok().build();
    }
}
