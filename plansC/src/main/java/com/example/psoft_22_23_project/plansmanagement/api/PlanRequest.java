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

import lombok.Data;

@Data
public class PlanRequest {
	private String name;
	private String description;
	private String numberOfMinutes;
	private String musicSuggestion;
	private String maximumNumberOfUsers;
	private String musicCollection;
	private String annualFee;
	private String monthlyFee;
	private String active;
	private String promoted;

	public String getAnnualFee() {
		String numericPart = annualFee.replaceAll("[^\\d.]", ""); // Removes non-numeric characters except '.'
		return numericPart;
	}

	public String getMonthlyFee() {
		String numericPart = monthlyFee.replaceAll("[^\\d.]", ""); // Removes non-numeric characters except '.'

		return numericPart;
	}

	public PlanRequest(String name, String description, String numberOfMinutes, String maximumNumberOfUsers, String musicCollection, String musicSuggestion, String annualFee, String monthlyFee, String active, String promoted) {
		this.name = name;
		this.description = description;
		this.numberOfMinutes = numberOfMinutes;
		this.maximumNumberOfUsers = maximumNumberOfUsers;
		this.musicCollection = musicCollection;
		this.musicSuggestion = musicSuggestion;
		this.annualFee = annualFee;
		this.monthlyFee = monthlyFee;
		this.active = active;
		this.promoted = promoted;
	}
}
