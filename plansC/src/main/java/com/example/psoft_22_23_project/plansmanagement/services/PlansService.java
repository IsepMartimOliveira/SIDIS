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
import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequestBonus;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;

import java.io.IOException;
import java.net.URISyntaxException;

public interface PlansService {

	Plans create(CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException;

	Plans partialUpdate(String name, EditPlansRequest resource,String auth, long parseLong) throws URISyntaxException, IOException, InterruptedException;

	Plans deactivate(String name, String authorizationToken,long desiredVersion);

	Plans createBonus(CreatePlanRequestBonus resource);
}
