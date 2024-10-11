package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.service.RecipeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeServiceImpl recipeService;

    @Autowired
    public RecipeController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    // 페이지 번호로 요리 레시피 목록 조회
    @GetMapping
    public ResponseDTO<Page<RecipeDTO>> getRecipeByPageNo(@RequestParam("page") Integer pageNo) {
        Page<RecipeDTO> recipeDTOPage = recipeService.findRecipeByPageNo(pageNo);
        return ResponseDTO.ok(recipeDTOPage);
    }

    // 요리 레시피 단건 조회
    @GetMapping("/{recipeId}")
    public ResponseDTO<RecipeDTO> getRecipeByRecipeId(@PathVariable("recipeId") Long recipeId) {
        RecipeDTO recipeDTO = recipeService.findRecipeByRecipeId(recipeId);
        return ResponseDTO.ok(recipeDTO);
    }

}
