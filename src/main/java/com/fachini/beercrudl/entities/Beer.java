package com.fachini.beercrudl.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Beer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 500)
    private String description;

    @NotNull
    @Size(max = 200)
    private String harmonization;

    @NotNull
    @Size(max = 20)
    @Enumerated(EnumType.STRING)
    private BeerColor color;

    @NotNull
    @Column(name = "alcoholic_strength")
    private Double alcoholicStrength;

    @NotNull
    private Double temperature;

    @NotNull
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
