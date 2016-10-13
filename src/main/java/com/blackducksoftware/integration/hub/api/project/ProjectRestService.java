/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.blackducksoftware.integration.hub.api.project;

import static com.blackducksoftware.integration.hub.api.UrlConstants.SEGMENT_API;
import static com.blackducksoftware.integration.hub.api.UrlConstants.SEGMENT_PROJECTS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Method;

import com.blackducksoftware.integration.hub.api.HubItemRestService;
import com.blackducksoftware.integration.hub.api.HubRequest;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.exception.ProjectDoesNotExistException;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class ProjectRestService extends HubItemRestService<ProjectItem> {
	private static final List<String> PROJECTS_SEGMENTS = Arrays.asList(SEGMENT_API, SEGMENT_PROJECTS);

	private static final Type ITEM_TYPE = new TypeToken<ProjectItem>() {
	}.getType();
	private static final Type ITEM_LIST_TYPE = new TypeToken<List<ProjectItem>>() {
	}.getType();

	public ProjectRestService(final RestConnection restConnection, final Gson gson, final JsonParser jsonParser) {
		super(restConnection, gson, jsonParser, ITEM_TYPE, ITEM_LIST_TYPE);
	}

	public List<ProjectItem> getAllProjects() throws IOException, BDRestException, URISyntaxException {
		final HubRequest projectItemRequest = new HubRequest(getRestConnection(), getJsonParser());
		projectItemRequest.setMethod(Method.GET);
		projectItemRequest.setLimit(100);
		projectItemRequest.addUrlSegments(PROJECTS_SEGMENTS);

		final JsonObject jsonObject = projectItemRequest.executeForResponseJson();
		final List<ProjectItem> allProjectItems = getAll(jsonObject, projectItemRequest);
		return allProjectItems;
	}

	public List<ProjectItem> getAllProjectMatches(final String projectName)
			throws IOException, BDRestException, URISyntaxException {
		final HubRequest projectItemRequest = new HubRequest(getRestConnection(), getJsonParser());
		projectItemRequest.setMethod(Method.GET);
		projectItemRequest.setLimit(100);
		projectItemRequest.addUrlSegments(PROJECTS_SEGMENTS);
		if (StringUtils.isNotBlank(projectName)) {
			projectItemRequest.setQ("name:" + projectName);
		}

		final JsonObject jsonObject = projectItemRequest.executeForResponseJson();
		final List<ProjectItem> allProjectItems = getAll(jsonObject, projectItemRequest);
		return allProjectItems;
	}

	public List<ProjectItem> getProjectMatches(final String projectName, final int limit)
			throws IOException, BDRestException, URISyntaxException {
		final HubRequest projectItemRequest = new HubRequest(getRestConnection(), getJsonParser());
		projectItemRequest.setMethod(Method.GET);
		projectItemRequest.setLimit(limit);
		projectItemRequest.addUrlSegments(PROJECTS_SEGMENTS);
		if (StringUtils.isNotBlank(projectName)) {
			projectItemRequest.setQ("name:" + projectName);
		}

		final JsonObject jsonObject = projectItemRequest.executeForResponseJson();
		final List<ProjectItem> allProjectItems = getItems(jsonObject);
		return allProjectItems;
	}

	public ProjectItem getProjectByName(String projectName)
			throws IOException, BDRestException, URISyntaxException, ProjectDoesNotExistException {
		projectName = StringUtils.trimToEmpty(projectName);
		final List<ProjectItem> allProjectItems = getAllProjectMatches(projectName);
		for (final ProjectItem project : allProjectItems) {
			if (projectName.equals(project.getName())) {
				return project;
			}
		}
		throw new ProjectDoesNotExistException("This Project does not exist. Project : " + projectName);
	}

}