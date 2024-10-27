package com.avengers.yoribogo.recipe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "RECIPE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Recipe {

    @Id
    @Column(name = "RECIPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(name = "MENU_NAME")
    private String menuName;

    @Column(name = "MENU_INGREDIENT")
    private String menuIngredient;

    @Column(name = "MENU_IMAGE")
    private String menuImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "MENU_TYPE")
    private MenuType menuType;

    @Column(name = "USER_ID")
    private Long userId;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeManual> recipeManuals;

}
