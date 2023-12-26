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

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class PlansBootstrapper implements CommandLineRunner {

	private final PlansRepositoryDB plansRepo;
	@Value("${server.port}")
	private int currentPort;

	@Override
	public void run(final String... args) throws Exception {

			if (plansRepo.findByName_Name("Free").isEmpty()) {

				final Name name = new Name();
				name.setName("Free");

				final Description description = new Description();
				description.setDescription("Based Plan Free");

				final NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
				numberOfMinutes.setNumberOfMinutes("1000");

				final MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
				maximumNumberOfUsers.setMaximumNumberOfUsers(1);

				final MusicCollection musicCollection = new MusicCollection();
				musicCollection.setMusicCollection(0);

				final MusicSuggestion musicSuggestion = new MusicSuggestion();
				musicSuggestion.setMusicSuggestion("automatic");

				final AnnualFee annualFee = new AnnualFee();
				annualFee.setAnnualFee(00.00);

				final MonthlyFee monthlyFee = new MonthlyFee();
				monthlyFee.setMonthlyFee(00.00);

				final Active active = new Active();
				active.setActive(true);

				final Promoted promoted = new Promoted();
				promoted.setPromoted(false);


				final Plans plans = new Plans(name , description,numberOfMinutes, maximumNumberOfUsers,musicCollection,musicSuggestion,annualFee,monthlyFee,active,promoted);

				plansRepo.save(plans);
			}
			if (plansRepo.findByName_Name("Silver").isEmpty()) {
				final Name name = new Name();
				name.setName("Silver");

				final Description description = new Description();
				description.setDescription("Based Plan Silver");

				final NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
				numberOfMinutes.setNumberOfMinutes("5000");

				final MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
				maximumNumberOfUsers.setMaximumNumberOfUsers(3);

				final MusicCollection musicCollection = new MusicCollection();
				musicCollection.setMusicCollection(10);

				final MusicSuggestion musicSuggestion = new MusicSuggestion();
				musicSuggestion.setMusicSuggestion("automatic");

				final AnnualFee annualFee = new AnnualFee();
				annualFee.setAnnualFee(49.90);

				final MonthlyFee monthlyFee = new MonthlyFee();
				monthlyFee.setMonthlyFee(4.99);

				final Active active = new Active();
				active.setActive(true);

				final Promoted promoted = new Promoted();
				promoted.setPromoted(false);


				final Plans plans = new Plans(name , description,numberOfMinutes, maximumNumberOfUsers,musicCollection,musicSuggestion,annualFee,monthlyFee,active,promoted);
				plansRepo.save(plans);
			}
			if (plansRepo.findByName_Name("Gold").isEmpty()) {
				final Name name = new Name();
				name.setName("Gold");

				final Description description = new Description();
				description.setDescription("Based Plan Gold");

				final NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
				numberOfMinutes.setNumberOfMinutes("unlimited");

				final MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
				maximumNumberOfUsers.setMaximumNumberOfUsers(6);

				final MusicCollection musicCollection = new MusicCollection();
				musicCollection.setMusicCollection(25);

				final MusicSuggestion musicSuggestion = new MusicSuggestion();
				musicSuggestion.setMusicSuggestion("personalized");

				final AnnualFee annualFee = new AnnualFee();
				annualFee.setAnnualFee(59.90);

				final MonthlyFee monthlyFee = new MonthlyFee();
				monthlyFee.setMonthlyFee(5.99);

				final Active active = new Active();
				active.setActive(true);

				final Promoted promoted = new Promoted();
				promoted.setPromoted(true);

				final Plans plans = new Plans(name , description,numberOfMinutes, maximumNumberOfUsers,musicCollection,musicSuggestion,annualFee,monthlyFee,active,promoted);
				plansRepo.save(plans);
			}
		}
	}


