package com.avengers.yoribogo.recipe.dto;

import com.avengers.yoribogo.recipe.domain.MenuType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class RecipeDTO extends BaseRecipeDTO {

    @JsonProperty("menu_type")
    private MenuType menuType;

    @JsonProperty("user_id")
    private Long userId;

}
