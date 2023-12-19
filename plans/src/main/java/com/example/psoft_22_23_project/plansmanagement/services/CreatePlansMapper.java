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
package com.example.psoft_22_23_project.plansmanagement.services;
import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CreatePlansMapper {
	@Mapping(source = "request.name", target = "name.name")
	@Mapping(source = "request.description", target = "description.description")
	@Mapping(source = "request.numberOfMinutes", target = "numberOfMinutes.numberOfMinutes")
	@Mapping(source = "request.maximumNumberOfUsers", target = "maximumNumberOfUsers.maximumNumberOfUsers")
	@Mapping(source = "request.musicCollection", target = "musicCollection.musicCollection")
	@Mapping(source = "request.musicSuggestion", target = "musicSuggestion.musicSuggestion")
	@Mapping(source = "request.annualFee", target = "annualFee.annualFee")
	@Mapping(source = "request.monthlyFee", target = "monthlyFee.monthlyFee")
	@Mapping(source = "request.active", target = "active.active")
	@Mapping(source = "request.promoted", target = "promoted.promoted")
	@Mapping(source = "request.bonus", target = "bonus.bonus")

	public abstract Plans create(CreatePlanRequest request);

}
