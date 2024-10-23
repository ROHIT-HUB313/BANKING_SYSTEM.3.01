package dhaka.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dhaka.entity.Users;
import dhaka.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Fetch user from the database
		Optional<Users>user = userRepo.findByEmail(email);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
		// Return a UserDetails implementation
		return new MyUserDetails(user.get());
	}
}