package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.BaseRecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeWithManualsDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecommendDTO;
import com.avengers.yoribogo.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // 페이지 번호로 요리 레시피 목록 조회
    @GetMapping
    public ResponseDTO<?> getRecipeByPageNo(@RequestParam("page") Integer pageNo) {
        Page<RecipeDTO> recipeDTOPage = recipeService.findRecipeByPageNo(pageNo);
        return ResponseDTO.ok(recipeDTOPage);
    }

    // 요리 레시피 단건 조회
    @GetMapping("/{recipeId}")
    public ResponseDTO<?> getRecipeByRecipeId(@PathVariable("recipeId") Long recipeId) {
        RecipeWithManualsDTO recipeDTO = recipeService.findRecipeWithManualsByRecipeId(recipeId);
        return ResponseDTO.ok(recipeDTO);
    }

    // 요리 레시피 요리 이름으로 조회
    @GetMapping("/search")
    public ResponseDTO<?> getRecipeByMenuName(@RequestParam("name") String menuName,
                                                              @RequestParam("page") Integer pageNo) {
        Page<RecipeDTO> recipeDTOPage = recipeService.findRecipeByMenuName(menuName, pageNo);
        return ResponseDTO.ok(recipeDTOPage);
    }

    // 요리 레시피 등록
    @PostMapping
    public ResponseDTO<?> createRecipe(@RequestBody RecipeDTO registRecipeDTO) {
        RecipeDTO recipeDTO = recipeService.registRecipe(registRecipeDTO);
        return ResponseDTO.ok(recipeDTO);
    }

    // 요리 레시피 수정
    @PutMapping("/{recipeId}")
    public ResponseDTO<?> updateRecipe(@PathVariable("recipeId") Long recipeId,
                                               @RequestBody RecipeDTO modifyRecipeDTO) {
        RecipeDTO recipeDTO = recipeService.modifyRecipe(recipeId, modifyRecipeDTO);
        return ResponseDTO.ok(recipeDTO);
    }

    // 요리 레시피 삭제
    @DeleteMapping("/{recipeId}")
    public ResponseDTO<?> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.removeRecipe(recipeId);
        return ResponseDTO.ok(null);
    }

    // 요리 추천하기
    @PostMapping("/recommend")
    public ResponseDTO<?> createRecommendRecipe(@RequestBody RequestRecommendDTO requestRecommendDTO) {
        BaseRecipeDTO baseRecipeDTO = recipeService.registRecommendRecipe(requestRecommendDTO);
        return ResponseDTO.ok(baseRecipeDTO);
    }

}
