package dhaka.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dhaka.entity.BankAccount;
import dhaka.entity.Transaction;
import dhaka.entity.Users;
import dhaka.repository.UserRepo;

@Service
public class UserService {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@Autowired
	private UserRepo userRepo;

	/*
	 * private final UserRepo userRepo; 
	 * public UserService(UserRepo userRepo) {
	 * this.userRepo = userRepo; }
	 */

	public void createUser(Users user) {
		if (user.getAge() < 18)
			throw new IllegalArgumentException("User must be 18+");

		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("USER");

		BankAccount bankAccount = new BankAccount();

		/*
		 * make a payment gateway directed here before finishing process, created
		 * bankAccount instance is the new pop up page in which user submit its payment
		 * in her/his account managed by Company Admin person.
		 */
		bankAccount.setBalance(2000);
		user.setBankAccount(bankAccount);
		bankAccount.setUser(user);

		userRepo.save(user);
	}

	public Users login(String email, String password) {
		Users user = userRepo.findByEmail(email).orElseThrow();
		if (!encoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Invalid credentials");
		}

		return user;
	}

	public void resetPassword(String email, String oldPassword, String newPassword) {
		Users user = userRepo.findByEmail(email).orElseThrow();
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(newPassword));// new password is also encrypted
		} else {
			throw new IllegalArgumentException("Invalid credentials");
		}
		userRepo.save(user);
	}

	public BankAccount fetchUserBalance(String name) {
		Users user = userRepo.findByName(name);
		if (user == null) {
			throw new NoSuchElementException();
		}
		// If a value is present, returns the value, otherwise throws "NoSuchElementException".
		return user.getBankAccount();
	}

	public List<Transaction> getTransactionHistory(String name) {
		Users user = userRepo.findByName(name);
		if (user == null) {
			throw new NoSuchElementException();
		}
		BankAccount bankAcc = user.getBankAccount();
		return bankAcc.getTransactions();
	}
}
