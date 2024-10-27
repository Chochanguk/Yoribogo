package com.avengers.yoribogo.recipe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RECIPE_MANUAL")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeManual {

    @Id
    @Column(name = "RECIPE_MANUAL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeManualId;

    @Column(name = "RECIPE_MANUAL_STEP")
    private Integer recipeManualStep;

    @Column(name = "MANUAL_MENU_IMAGE")
    private String manualMenuImage;

    @Column(name = "MANUAL_CONTENT")
    private String manualContent;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    private Recipe recipe;

}
