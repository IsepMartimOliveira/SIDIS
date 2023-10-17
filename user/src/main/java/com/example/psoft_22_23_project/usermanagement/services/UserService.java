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
import com.example.psoft_22_23_project.filestoragemanagement.service.FileStorageService;
import com.example.psoft_22_23_project.usermanagement.api.UserEditMapper;
import com.example.psoft_22_23_project.usermanagement.api.UserViewMapper;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserImageRepository;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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

	private final UserRepository userRepo;
	private final UserViewMapper userViewMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserEditMapper userEditMapper;

	private final UserRepository userRepository;

	private final UserImageRepository userImageRepository;
	private final FileStorageService fileStorageService;

	@Transactional
	public User create(final CreateUserRequest request) throws IOException, InterruptedException, URISyntaxException {

		Optional<User> user2 = userRepository.findByUsername(request.getUsername());

		if (user2.isPresent()) {
			throw new ConflictException("Username already exists locally!");
		}

		HttpResponse<String> response = userRepository.getUserFromOtherAPI(request.getUsername());

		if (response.statusCode() == 200) {
			throw new IllegalArgumentException("Username with name " + request.getUsername() + " already exists on another machine!");
		}
		else if(response.statusCode()==401){
			throw new IllegalArgumentException("Authentication failed. Please check your credentials or login to access this resource.");
		}

		if (!request.getPassword().equals(request.getRePassword())) {
			throw new ValidationException("Passwords don't match!");
		}



		final User user = userEditMapper.create(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		return userRepo.save(user);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

	public Iterable<User> findAll() {

		return userRepository.findAll();

	}
	public Optional<User> getUserByName(String Username) {return userRepository.findByUsername(Username);}

}
