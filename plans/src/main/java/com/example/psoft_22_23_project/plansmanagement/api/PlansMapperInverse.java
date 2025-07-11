/*
 * Copyright (c) 2022-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.model.PromotionResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class PlansMapperInverse {
	@Mapping(source = "plans.name", target = "name.name")
	@Mapping(source = "plans.description", target = "description.description")
	@Mapping(source = "plans.numberOfMinutes", target = "numberOfMinutes.numberOfMinutes")
	@Mapping(source = "plans.maximumNumberOfUsers", target = "maximumNumberOfUsers.maximumNumberOfUsers")
	@Mapping(source = "plans.musicCollection", target = "musicCollection.musicCollection")
	@Mapping(source = "plans.musicSuggestion", target = "musicSuggestion.musicSuggestion")
	@Mapping(source = "plans.annualFee", target = "annualFee.annualFee")
	@Mapping(source = "plans.monthlyFee", target = "monthlyFee.monthlyFee")
	@Mapping(source = "plans.active", target = "active.active")
	@Mapping(source = "plans.promoted", target = "promoted.promoted")
	public abstract Plans toPlansView(PlanRequest plans);

	@Mapping(source = "newplans.newPromotedPlan.name", target = "newPromotedPlan.name.name")
	@Mapping(source = "newplans.newPromotedPlan.description", target = "newPromotedPlan.description.description")
	@Mapping(source = "newplans.newPromotedPlan.numberOfMinutes", target = "newPromotedPlan.numberOfMinutes.numberOfMinutes")
	@Mapping(source = "newplans.newPromotedPlan.maximumNumberOfUsers", target = "newPromotedPlan.maximumNumberOfUsers.maximumNumberOfUsers")
	@Mapping(source = "newplans.newPromotedPlan.musicCollection", target = "newPromotedPlan.musicCollection.musicCollection")
	@Mapping(source = "newplans.newPromotedPlan.musicSuggestion", target = "newPromotedPlan.musicSuggestion.musicSuggestion")
	@Mapping(source = "newplans.newPromotedPlan.annualFee", target = "newPromotedPlan.annualFee.annualFee")
	@Mapping(source = "newplans.newPromotedPlan.monthlyFee", target = "newPromotedPlan.monthlyFee.monthlyFee")
	@Mapping(source = "newplans.newPromotedPlan.active", target = "newPromotedPlan.active.active")
	@Mapping(source = "newplans.newPromotedPlan.promoted", target = "newPromotedPlan.promoted.promoted")
	@Mapping(source = "newplans.previousPromotedPlan.name", target = "previousPromotedPlan.name.name")
	@Mapping(source = "newplans.previousPromotedPlan.description", target = "previousPromotedPlan.description.description")
	@Mapping(source = "newplans.previousPromotedPlan.numberOfMinutes", target = "previousPromotedPlan.numberOfMinutes.numberOfMinutes")
	@Mapping(source = "newplans.previousPromotedPlan.maximumNumberOfUsers", target = "previousPromotedPlan.maximumNumberOfUsers.maximumNumberOfUsers")
	@Mapping(source = "newplans.previousPromotedPlan.musicCollection", target = "previousPromotedPlan.musicCollection.musicCollection")
	@Mapping(source = "newplans.previousPromotedPlan.musicSuggestion", target = "previousPromotedPlan.musicSuggestion.musicSuggestion")
	@Mapping(source = "newplans.previousPromotedPlan.annualFee", target = "previousPromotedPlan.annualFee.annualFee")
	@Mapping(source = "newplans.previousPromotedPlan.monthlyFee", target = "previousPromotedPlan.monthlyFee.monthlyFee")
	@Mapping(source = "newplans.previousPromotedPlan.active", target = "previousPromotedPlan.active.active")
	@Mapping(source = "newplans.previousPromotedPlan.promoted", target = "previousPromotedPlan.promoted.promoted")
	public abstract PromotionResult toPlansViewsPromote(PlanRequestPromoted newplans);


}
