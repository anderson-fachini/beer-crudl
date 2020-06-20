package com.fachini.beercrudl.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fachini.beercrudl.entities.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    @Modifying
    @Query("update Beer b set b.hasImage = true, b.imageType = :imageType where b.id = :id")
    void updateImageDefinition(@Param("imageType") String imageType, @Param("id") UUID id);
}
