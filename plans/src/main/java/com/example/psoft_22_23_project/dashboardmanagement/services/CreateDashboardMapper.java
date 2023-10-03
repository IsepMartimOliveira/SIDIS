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
package com.example.psoft_22_23_project.dashboardmanagement.services;
import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CreateDashboardMapper {
	@Mapping(source = "active", target = "totalActiveSubscriptions.totalActiveSubscriptions")
	@Mapping(source = "canceled", target = "totalCanceledSubscriptions.totalCanceledSubscriptions")
	public abstract Dashboard create(Integer active, Integer canceled);

	@Mapping(source = "active", target = "totalActiveSubscriptions.totalActiveSubscriptions")
	public abstract Dashboard createActive(Integer active);

	@Mapping(source = "canceled", target = "totalCanceledSubscriptions.totalCanceledSubscriptions")
	public abstract Dashboard createCancelled(Integer canceled);

	@Mapping(source = "revenue", target = "totalRevenue.totalRevenue")
	public abstract Dashboard createRevenueTillNow(Double revenue);

}
