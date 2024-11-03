package com.lake.lakeSidehotel.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lake.lakeSidehotel.exception.RoleNotFoundException;
import com.lake.lakeSidehotel.exception.UserAlreadyExistsException;
import com.lake.lakeSidehotel.model.Role;
import com.lake.lakeSidehotel.model.User;
import com.lake.lakeSidehotel.repository.RoleRepository;
import com.lake.lakeSidehotel.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	
	@Override
	public User registerUser(User user) {
		//kiem tra xem email da ton tai chua
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(user.getEmail() + " already exists");
		}
		//ma hoa mat khau
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		//tim kiem vai tro "ROLE_USER" và xử lý ngoại lệ nếu không tìm thấy
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
		
		//đặt vai trò cho người dùng
		user.setRoles(Collections.singletonList(userRole));
		
		//lưu người dùng
		return userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String email) {
		User theUser = getUser(email);
		if(theUser != null) {
			userRepository.deleteByEmail(email);
		}
		
	}

	@Override
	public User getUser(String email) {
		
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
