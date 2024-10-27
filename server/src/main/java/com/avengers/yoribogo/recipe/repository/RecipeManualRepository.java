package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.RecipeManual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeManualRepository extends JpaRepository<RecipeManual, Long> {

    @Query("SELECT rm FROM RecipeManual rm WHERE rm.recipe.recipeId = :recipeId")
    List<RecipeManual> findByRecipeId(Long recipeId);

}
