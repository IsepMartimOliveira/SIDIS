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
import com.example.psoft_22_23_project.usermanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
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
@Primary
public interface UserRepository extends UserRepoCustom, CrudRepository<User, Long> {

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


interface UserRepoCustom {
	HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;
}



@RequiredArgsConstructor
class UserRepoCustomImpl implements UserRepoCustom {
	// 82 91 subs
	// 81 90 plans
	// 83 92 users
	@Value("${server.port}")
	private int currentPort;
	@Override
	public HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {

		int otherPort = (currentPort == 8083) ? 8092 : 8083;
		URI uri = new URI("http://localhost:" + otherPort + "/api/user2/" + name);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;

	}



}