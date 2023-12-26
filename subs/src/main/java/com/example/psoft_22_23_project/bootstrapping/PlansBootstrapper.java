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
package com.example.psoft_22_23_project.bootstrapping;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.PlansManager;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class PlansBootstrapper implements CommandLineRunner {

	private final PlansManager plansRepo;
	@Value("${server.port}")
	private int currentPort;

	@Override
	public void run(final String... args) throws Exception {

			if (plansRepo.findPlan("Free").isEmpty()) {

				final String name = "Free";
				final String description = "Based Plan Free";
				final String numberOfMinutes = "1000";
				final String maximumNumberOfUsers = "1";
				final String musicCollection = "0";
				final String musicSuggestion = "automatic";
				final String annualFee = "00.00";
				final String monthlyFee = "00.00";
				final String active = "true";
				final String promoted = "false";
				final String bonus="false";

				final PlansDetails planDetails = new PlansDetails(name, description, numberOfMinutes, maximumNumberOfUsers, musicCollection, musicSuggestion, annualFee, monthlyFee, active, promoted,bonus);


				plansRepo.storePlan(planDetails);
			}
			if (plansRepo.findPlan("Silver").isEmpty()) {
				final String name = "Silver";
				final String description = "Based Plan Silver";
				final String numberOfMinutes = "5000";
				final String maximumNumberOfUsers ="3";
				final String musicCollection = "10";
				final String musicSuggestion = "automatic";
				final String annualFee = "49.90";
				final String monthlyFee = "4.99";
				final String active = "true";
				final String promoted = "false";
				final String bonus="false";


				final PlansDetails planDetails = new PlansDetails(name , description,numberOfMinutes, maximumNumberOfUsers,musicCollection,musicSuggestion,annualFee,monthlyFee,active,promoted,bonus);
				plansRepo.storePlan(planDetails);
			}
			if (plansRepo.findPlan("Gold").isEmpty()) {
				final String name = "Gold";
				final String description = "Based Plan Gold";
				final String numberOfMinutes = "unlimited";
				final String maximumNumberOfUsers = "6";
				final String musicCollection = "25";
				final String musicSuggestion = "personalized";
				final String annualFee = "59.90";
				final String monthlyFee = "5.99";
				final String active = "true";
				final String promoted = "true";
				final String bonus="false";


				final PlansDetails plans = new PlansDetails(name , description,numberOfMinutes, maximumNumberOfUsers,musicCollection,musicSuggestion,annualFee,monthlyFee,active,promoted,bonus);
				plansRepo.storePlan(plans);
			}
		}
	}


