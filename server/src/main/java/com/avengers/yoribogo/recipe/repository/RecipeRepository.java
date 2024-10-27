package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.Recipe;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    // 요리 레시피를 매뉴얼과 같이 단건 조회
    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.recipeManuals rm WHERE r.recipeId = :recipeId")
    Recipe findRecipeWithManuals(@Param("recipeId") Long recipeId);

    // 요리 이름이 포함된 레시피 목록을 페이지네이션 처리하여 반환
    Page<Recipe> findByMenuNameContaining(String menuName, Pageable pageable);

    // 요리 이름이 포함된 레시피 목록을 리스트로 반환
    List<Recipe> findByMenuNameContaining(String menuName);

}
