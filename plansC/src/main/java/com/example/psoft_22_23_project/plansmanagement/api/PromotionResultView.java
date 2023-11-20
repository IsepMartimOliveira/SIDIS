package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A Plan Promotion Result")
public class PromotionResultView {
    @Schema(description = "The newly promoted plan")
    private PlansView newPromotedPlan;

    @Schema(description = "The previous promoted plan")
    private PlansView previousPromotedPlan;
}

