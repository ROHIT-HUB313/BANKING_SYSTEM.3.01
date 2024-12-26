package dhaka.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dhaka.filter.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;
import dhaka.entity.BankAccount;
import dhaka.entity.Transaction;
import dhaka.entity.Users;
import dhaka.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;


	@GetMapping("/csrf")
	public CsrfToken getToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}

	@PostMapping("/signup")
	public ResponseEntity<String> createUserIdentity(@RequestBody Users user) {
		userService.createUser(user);
		return ResponseEntity.ok("USER -> " + user.getName() + "'s account created successfully.");
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String,String>> loginUser(@RequestParam("email") String email,
														@RequestParam("password") String password) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String role = userDetails.getAuthorities().iterator().next().getAuthority();
		String token = jwtUtils.generateToken(userDetails.getUsername(),role);

		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		userService.resetPassword(email, oldPassword, newPassword);
		return ResponseEntity.ok("Password reset successful");
	}

	@GetMapping("/balance/{name}")
	public ResponseEntity<BankAccount> getUserBalance(@PathVariable("name") String name) {
		BankAccount bankAcc = userService.fetchUserBalance(name);
		return ResponseEntity.ok(bankAcc);
	}

	@GetMapping("/transactions/{name}")
	public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable("name") String name) {
		List<Transaction> getTransactions = userService.getTransactionHistory(name);
		return ResponseEntity.ok(getTransactions);
	}
}
