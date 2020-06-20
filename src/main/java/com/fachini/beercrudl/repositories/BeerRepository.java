package com.fachini.beercrudl.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fachini.beercrudl.entities.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
