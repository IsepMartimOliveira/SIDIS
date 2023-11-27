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
package com.example.psoft_22_23_project.usermanagement.services;

import com.example.psoft_22_23_project.exceptions.ConflictException;
import com.example.psoft_22_23_project.rabbitMQ.UserCOMSender;
import com.example.psoft_22_23_project.usermanagement.api.UserEditMapper;
import com.example.psoft_22_23_project.usermanagement.api.UserMapperInverse;
import com.example.psoft_22_23_project.usermanagement.api.UserRequest;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final PasswordEncoder passwordEncoder;
	private final UserEditMapper userEditMapper;
	private final UserRepository userRepository;
	private final UserCOMSender userCOMSender;
	@Transactional
	public User create(final CreateUserRequest request) {

		Optional<User> User= userRepository.findByUsername(request.getUsername());

		if (User.isPresent()) {
			throw new ConflictException("Username already exists locally!");
		}

		if (!request.getPassword().equals(request.getRePassword())) {
			throw new ValidationException("Passwords don't match!");
		}

		final User user = userEditMapper.create(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		userCOMSender.send(request);

		return user;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

	public Iterable<User> findAll() {

		return userRepository.findAll();

	}
	public Optional<User> getUserByName(String username) throws URISyntaxException, IOException, InterruptedException {

		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return user;
		} else {
			Optional<User> obj = userRepository.getUserByNameNotLocally(username);
			return obj;

		}

	}


	public Optional<User> getUserByNameExternal(String username){
		Optional<User> user = userRepository.findByUsername(username);
		return user;

	}

	public void storeUser(CreateUserRequest userRequest) {
		Optional<User> User= userRepository.findByUsername(userRequest.getUsername());

		if (User.isPresent()) {
			throw new ConflictException("Username already exists locally!");
		}



		if (!userRequest.getPassword().equals(userRequest.getRePassword())) {
			throw new ValidationException("Passwords don't match!");
		}

		final User user = userEditMapper.create(userRequest);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

		 userRepository.save(user);
	}
}
