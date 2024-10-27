package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.recipe.domain.MenuType;
import com.avengers.yoribogo.recipe.dto.BaseRecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeWithManualsDTO;
import com.avengers.yoribogo.recipe.dto.RequestRecommendDTO;
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
        Assertions.assertFalse(recipeDTOPage.getContent().isEmpty(), "레시피 페이지가 비어 있습니다.");

        // 요소를 로그로 찍기
        for (RecipeDTO recipe : recipeDTOPage.getContent()) {
            log.info(recipe.toString());
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

    @DisplayName("요리 레시피와 매뉴얼 같이 단건 조회")
    @Test
    void testFindRecipeWithManualsByRecipeId() {
        // Given
        Long recipeId = 1L;

        // When
        RecipeWithManualsDTO recipeDTO = recipeService.findRecipeWithManualsByRecipeId(recipeId);

        // Then
        Assertions.assertNotNull(recipeDTO, "레시피가 null 입니다.");

        // 요소를 로그로 찍기
        log.info(recipeDTO.toString());
    }

    @DisplayName("요리 레시피 요리 이름으로 조회 테스트")
    @Test
    void testFindRecipeByMenuName() {
        // Given
        String menuName = "김치찌개";
        Integer pageNo = 1;

        // When
        Page<RecipeDTO> recipeDTOPage =
                recipeService.findRecipeByMenuName(menuName, pageNo);

        // Then
        Assertions.assertNotNull(recipeDTOPage, "레시피 페이지가 null 입니다.");
        Assertions.assertFalse(recipeDTOPage.getContent().isEmpty(), "레시피 페이지가 비어 있습니다.");

        // 요소를 로그로 찍기
        for (RecipeDTO recipe : recipeDTOPage.getContent()) {
            log.info(recipe.toString());
        }
    }

    @DisplayName("요리 레시피 등록 테스트")
    @Test
    void testRegistRecipe() {
        // Given
        RecipeDTO recipeDTO = RecipeDTO
                .builder()
                .menuName("AI요리이름")
                .menuIngredient("AI요리재료")
                .menuType(MenuType.AI)
                .userId(1L)
                .build();

        // When
        recipeDTO = recipeService.registRecipe(recipeDTO);

        // Then
        Assertions.assertNotNull(recipeDTO, "레시피가 null 입니다.");

        // 요소를 로그로 찍기
        log.info(recipeDTO.toString());
    }

    @DisplayName("요리 레시피 수정 테스트")
    @Test
    void testModifyRecipe() {
        // Given
        Long recipeId = 1L;
        RecipeDTO recipeDTO = RecipeDTO
                .builder()
                .menuName("김치찌개")
                .menuIngredient("김치")
                .menuImage("http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00028_2.png")
                .userId(1L)
                .build();

        // When
        recipeDTO = recipeService.modifyRecipe(recipeId, recipeDTO);

        // Then
        Assertions.assertNotNull(recipeDTO, "레시피가 null 입니다.");

        // 요소를 로그로 찍기
        log.info(recipeDTO.toString());
    }

    @DisplayName("요리 레시피 삭제 테스트")
    @Test
    void testRemoveRecipe() {
        // Given
        Long recipeId = 1L;

        // When
        recipeService.removeRecipe(recipeId);

        // Then
        Assertions.assertThrows(CommonException.class,
            () -> recipeService.removeRecipe(recipeId)
            , "요리 레시피가 삭제되지 않았습니다."
        );
    }

    @DisplayName("요리 추천하기 테스트")
    @Test
    void testRegistRecommendRecipe() {
        // Given
        RequestRecommendDTO requestRecommendDTO = RequestRecommendDTO
                .builder()
                .first("맑아요")
                .second("좋아요!")
                .third("혼자 먹어요")
                .fourth("아니요")
                .fifth("없어요")
                .build();

        // When
        BaseRecipeDTO baseRecipeDTO = recipeService.registRecommendRecipe(requestRecommendDTO);

        // Then
        Assertions.assertNotNull(baseRecipeDTO, "추천된 요리가 null 입니다.");

        // 로그 찍기
        log.info("(recipeId: {}, menuName: {}, menuIngredient: {}, menuImage: {})",
                baseRecipeDTO.getRecipeId(), baseRecipeDTO.getMenuName(), baseRecipeDTO.getMenuIngredient(),
                baseRecipeDTO.getMenuImage());
    }

}
