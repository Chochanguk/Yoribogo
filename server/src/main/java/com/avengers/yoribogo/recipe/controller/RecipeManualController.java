package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.RecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestAIRecipeManualDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecipeManualDTO;
import com.avengers.yoribogo.recipe.service.RecipeManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/manuals")
public class RecipeManualController {

    private final RecipeManualService recipeManualService;

    @Autowired
    public RecipeManualController(RecipeManualService recipeManualService) {
        this.recipeManualService = recipeManualService;
    }

    // 요리 레시피 아이디로 매뉴얼 조회
    @GetMapping
    public ResponseDTO<?> getRecipeManual(@RequestParam("recipe") Long recipeId) {
        List<RecipeManualDTO> recipeManualDTOList = recipeManualService.findRecipeManualByRecipeId(recipeId);
        return ResponseDTO.ok(recipeManualDTOList);
    }

    // 요리 레시피 매뉴얼 등록
    @PostMapping
    public ResponseDTO<?> createRecipeManual(@RequestParam("recipe") Long recipeId,
                                          @RequestBody RequestRecipeManualDTO requestRecipeManualDTO) {
        List<RecipeManualDTO> recipeManualDTOList =
                recipeManualService.registRecipeManual(recipeId, requestRecipeManualDTO);
        return ResponseDTO.ok(recipeManualDTOList);
    }

    // AI 요리 레시피 매뉴얼 등록
    @PostMapping(value = "/ai", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createAIRecipeManual(@RequestParam("recipe") Long recipeId,
                                             @RequestBody RequestAIRecipeManualDTO requestAIRecipeManualDTO) {
        return recipeManualService.registAIRecipeManual(recipeId, requestAIRecipeManualDTO);
    }

    // 요리 레시피 매뉴얼 수정
    @PutMapping
    public ResponseDTO<?> updateRecipeManual(@RequestParam("recipe") Long recipeId,
                                             @RequestBody RequestRecipeManualDTO requestRecipeManualDTO) {
        List<RecipeManualDTO> recipeManualDTOList =
                recipeManualService.modifyRecipeManual(recipeId, requestRecipeManualDTO);
        return ResponseDTO.ok(recipeManualDTOList);
    }

}
