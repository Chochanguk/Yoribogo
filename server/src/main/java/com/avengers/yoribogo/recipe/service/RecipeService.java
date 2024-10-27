package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.BaseRecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeWithManualsDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecommendDTO;
import org.springframework.data.domain.Page;

public interface RecipeService {

    // 페이지 번호로 요리 레시피 목록 조회
    Page<RecipeDTO> findRecipeByPageNo(Integer pageNo);

    // 요리 레시피 단건 조회
    RecipeDTO findRecipeByRecipeId(Long recipeId);

    // 요리 레시피 매뉴얼과 같이 단건 조회
    RecipeWithManualsDTO findRecipeWithManualsByRecipeId(Long recipeId);

    // 요리 레시피 요리 이름으로 조회
    Page<RecipeDTO> findRecipeByMenuName(String menuName, Integer pageNo);

    // 요리 레시피 등록
    RecipeDTO registRecipe(RecipeDTO registRecipeDTO);

    // 요리 레시피 수정
    RecipeDTO modifyRecipe(Long recipeId, RecipeDTO modifyRecipeDTO);

    // 요리 레시피 삭제
    void removeRecipe(Long recipeId);

    // 요리 추천하기
    BaseRecipeDTO registRecommendRecipe(RequestRecommendDTO requestRecommendDTO);

}
