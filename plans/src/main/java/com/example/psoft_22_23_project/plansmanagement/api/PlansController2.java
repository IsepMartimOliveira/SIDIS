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

import com.example.psoft_22_23_project.plansmanagement.services.PlansService2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;


@Tag(name = "Plans2", description = "Endpoints for managing plans2")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans2")
public class PlansController2 {

	private static final Logger logger = LoggerFactory.getLogger(PlansController2.class);

	private final PlansService2 service;

	private final PlansViewMapper plansViewMapper;

	private final FeeRevisionViewMapper feeRevisionViewMapper;

	private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
		if (ifMatchHeader.startsWith("\"")) {
			return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
		}
		return Long.parseLong(ifMatchHeader);
	}

	@Operation(summary = "Gets all plans")
	@GetMapping
	public Iterable<PlansView> findActive() throws URISyntaxException, IOException, InterruptedException {
		return plansViewMapper.toPlansView(service.findAtive());
	}



}


