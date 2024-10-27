package com.avengers.yoribogo.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecipeManualDTO {

    @JsonProperty("manual_id")
    private Long recipeManualId;

    @JsonProperty("manual_step")
    private Integer recipeManualStep;

    @JsonProperty("manual_image")
    private String manualMenuImage;

    @JsonProperty("manual_content")
    private String manualContent;

}
