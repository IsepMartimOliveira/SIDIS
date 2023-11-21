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
package com.example.psoft_22_23_project.usermanagement.repositories;


import com.example.psoft_22_23_project.exceptions.NotFoundException;
import com.example.psoft_22_23_project.usermanagement.api.UserMapperInverse;
import com.example.psoft_22_23_project.usermanagement.api.UserRequest;
import com.example.psoft_22_23_project.usermanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
@Configuration
public interface UserRepository extends  UserRepositoryDB,UserRepoHttp{

}
interface UserRepositoryDB extends CrudRepository<User,Long>{
	@Override
	@Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
			@CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
	<S extends User> S save(S entity);
	@Override
	@Cacheable
	Optional<User> findById(Long objectId);
	@Cacheable
	default User getById(final Long id) {
		final Optional<User> maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}
	@Cacheable
	Optional<User> findByUsername(String username);
}
interface UserRepoHttp {
	HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

	Optional<User> getUserByNameNotLocally(String username) throws URISyntaxException, IOException, InterruptedException;
}
@RequiredArgsConstructor
class UserRepoHttpImpl implements UserRepoHttp {

	private final UserMapperInverse userMapperInverse;

	// 82 91 subs
	// 81 90 plans
	// 83 92 users
	@Value("${server.port}")
	private int currentPort;
	@Value("${port1}")
	private int portOne;
	@Value("${port2}")
	private int portTwo;
	private int otherPort;

	@Value("${user.external}")
	private String externalUserUrl;

	@Override
	public HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {

		//int otherPort = (currentPort == 8083) ? 8092 : 8083;
		//URI uri = new URI("http://localhost:" + otherPort + "/api/user/external/" + name);
		String urlWithDynamicName = externalUserUrl.replace("{name}", name);
		URI uri = new URI(urlWithDynamicName);
		//otherPort=(currentPort==portTwo) ? portOne : portTwo;
		//URI uri = new URI("http://localhost:" + otherPort + "/api/user/external/"+name);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;

	}

	@Override
	public Optional<User> getUserByNameNotLocally(String username) throws URISyntaxException, IOException, InterruptedException {
		HttpResponse<String> plan = getUserFromOtherAPI(username);
		if (plan.statusCode() == 200) {
			JSONObject jsonArray = new JSONObject(plan.body());

			UserRequest newUser = new UserRequest(jsonArray.getString("username"));
			User obj = userMapperInverse.toUserView(newUser);
			return Optional.ofNullable(obj);
		}
		else {
			throw new IllegalArgumentException("Plan with name " + username + " already exists locally!");
		}
	}
}






