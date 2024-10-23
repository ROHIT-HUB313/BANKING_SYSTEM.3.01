package dhaka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password,
			@RequestParam boolean myBalance, @RequestParam boolean myTransactions) {
		Users user = userService.login(email, password);
		StringBuilder response = new StringBuilder("Login Successful for user: " + user.getName());

		if (myBalance) {
			BankAccount myAcc = userService.fetchUserBalance(user.getName());
			response.append("\nBALANCE: ").append(myAcc.getBalance());
		}

		if (myTransactions) {
			List<Transaction> myTransac = userService.getTransactionHistory(user.getName());
			response.append("\nTRANSACTIONS: ").append(myTransac);
		}

		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
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