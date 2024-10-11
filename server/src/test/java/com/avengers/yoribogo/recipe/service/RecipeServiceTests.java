package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class RecipeServiceTests {

    @Autowired
    private RecipeService recipeService;

    @DisplayName("요리 레시피 페이지별 조회 테스트")
    @Test
    void testFindRecipeByPageNo() {
        // Given
        Integer pageNo = 1;

        // When
        Page<RecipeDTO> recipeDTOPage = recipeService.findRecipeByPageNo(pageNo);

        // Then
        Assertions.assertNotNull(recipeDTOPage, "레시피 페이지가 null 입니다.");
        Assertions.assertFalse(recipeDTOPage.isEmpty(), "레시피 페이지가 비어 있습니다.");

        // 요소를 로그로 찍기
        for (RecipeDTO recipe : recipeDTOPage.getContent()) {
            log.info("레시피 ID: {}, 레시피 이름: {}, 레시피 타입: {}",
                    recipe.getRecipeId(),
                    recipe.getMenuName(),
                    recipe.getMenuType());
        }
    }

    @DisplayName("요리 레시피 단건 조회")
    @Test
    void testFindRecipeByRecipeId() {
        // Given
        Long recipeId = 1L;

        // When
        RecipeDTO recipeDTO = recipeService.findRecipeByRecipeId(recipeId);

        // Then
        Assertions.assertNotNull(recipeDTO, "레시피가 null 입니다.");

        // 요소를 로그로 찍기
        log.info(recipeDTO.toString());
    }

}
