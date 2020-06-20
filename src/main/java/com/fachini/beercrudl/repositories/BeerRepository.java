package com.fachini.beercrudl.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.fachini.beercrudl.entities.Beer;
import com.fachini.beercrudl.entities.QBeer;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

public interface BeerRepository extends CrudRepository<Beer, UUID>, //
        PagingAndSortingRepository<Beer, UUID>, //
        QuerydslPredicateExecutor<Beer>, //
        QuerydslBinderCustomizer<QBeer> {

    @Override
    default void customize(QuerydslBindings bindings, QBeer beer) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }

    @Modifying
    @Query("update Beer b set b.hasImage = true, b.imageType = :imageType where b.id = :id")
    void updateImageDefinition(@Param("imageType") String imageType, @Param("id") UUID id);
}
