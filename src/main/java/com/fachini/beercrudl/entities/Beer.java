package com.fachini.beercrudl.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(uniqueConstraints = { //
        @UniqueConstraint(name = EntityConstraints.UK_BEER_NAME, columnNames = { "name" })//
})
public class Beer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull(message = "{beer.name.null.message}")
    @Size(max = 50)
    private String name;

    @NotNull(message = "{beer.description.null.message}")
    @Size(max = 500)
    private String description;

    @NotNull(message = "{beer.harmonization.null.message}")
    @Size(max = 200)
    private String harmonization;

    @NotNull(message = "{beer.color.null.message}")
    @Enumerated(EnumType.STRING)
    private BeerColor color;

    @NotNull(message = "{beer.alcoholilc_strength.null.message}")
    @Column(name = "alcoholic_strength")
    private Double alcoholicStrength;

    @NotNull(message = "{beer.temperature.null.message}")
    private Double temperature;

    @NotNull(message = "{beer.ingredients.null.message}")
    @Size(max = 2000)
    private String ingredients;

    @Column(name = "has_image")
    private Boolean hasImage;

    @Column(name = "image_type")
    private String imageType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
