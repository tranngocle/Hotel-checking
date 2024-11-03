package com.lake.lakeSidehotel.service;

import java.util.List;
import com.lake.lakeSidehotel.model.User;

public interface IUserService {
	User registerUser(User user);
	List<User> getUsers();
	void deleteUser(String email);
	User getUser(String email);
	
}
