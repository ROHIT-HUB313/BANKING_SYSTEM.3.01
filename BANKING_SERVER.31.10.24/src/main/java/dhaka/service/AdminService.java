package dhaka.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhaka.entity.BankAccount;
import dhaka.entity.Transaction;
import dhaka.entity.Users;
import dhaka.repository.BankAccountRepo;
import dhaka.repository.TransactionRepo;
import dhaka.repository.UserRepo;
import jakarta.transaction.Transactional;

@Service
public class AdminService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BankAccountRepo bankAccountRepo;

	@Autowired
	private TransactionRepo transactionRepo;

	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}

	public Users getUserOnly(String name) {
		Users user = userRepo.findByName(name);
		if (user == null) {
			throw new NoSuchElementException();
		}
		return user;
	}

	public void deleteUser(Long id) {
		Users user = userRepo.findById(id).orElseThrow();
		BankAccount account = user.getBankAccount();

		// Delete user's transactions, bank account, and user itself
		userRepo.delete(user);
		bankAccountRepo.delete(account);
	}

	public void updateUserDetails(Long userId, Users updatedUser) {
		Users user = userRepo.findById(userId).orElseThrow();

		user.setName(updatedUser.getName());
		user.setFatherName(updatedUser.getFatherName());
		user.setMobileNo(updatedUser.getMobileNo());
		user.setEmail(updatedUser.getEmail());
		user.setAge(updatedUser.getAge());

		userRepo.save(user);
	}

	@Transactional
	public void deposit(Long accountNo, double amount) {
		BankAccount account = bankAccountRepo.findById(accountNo)
				.orElseThrow(() -> new NoSuchElementException("Account not found"));
		if (account.getBalance() >= 0) {
			account.setBalance(account.getBalance() + amount);
		}

		Transaction transaction = new Transaction();
		transaction.setType("Deposit");
		transaction.setAmount(amount);
		transaction.setDate(LocalDateTime.now());
		transaction.setBankAccTransaction(account);
		transactionRepo.save(transaction);

		bankAccountRepo.save(account);
	}

	@Transactional
	public void withdraw(Long accountNo, double amount) {
		BankAccount account = bankAccountRepo.findById(accountNo)
				.orElseThrow(() -> new NoSuchElementException("Account not found"));

		if (account.getBalance() + 2000 < amount) {
			throw new IllegalArgumentException("Insufficient funds");
		}

		account.setBalance(account.getBalance() - amount);

		Transaction transaction = new Transaction();
		transaction.setType("Withdraw");
		transaction.setAmount(amount);
		transaction.setDate(LocalDateTime.now());
		transaction.setBankAccTransaction(account);
		transactionRepo.save(transaction);

		bankAccountRepo.save(account);
	}

	@Transactional
	public void transfer(Long fromAccountNo, Long toAccountNo, double amount) {
		BankAccount fromAccount = bankAccountRepo.findById(fromAccountNo)
				.orElseThrow(() -> new NoSuchElementException("Sender account not found"));

		BankAccount toAccount = bankAccountRepo.findById(toAccountNo)
				.orElseThrow(() -> new NoSuchElementException("Receiver account not found"));

		if (fromAccount.getBalance() + 2000 < amount) {
			throw new IllegalArgumentException("Insufficient funds");
		}

		fromAccount.setBalance(fromAccount.getBalance() - amount);
		toAccount.setBalance(toAccount.getBalance() + amount);

		Transaction fromTransaction = new Transaction();
		fromTransaction.setType("Transfer (Debit)");
		fromTransaction.setAmount(amount);
		fromTransaction.setDate(LocalDateTime.now());
		fromTransaction.setBankAccTransaction(fromAccount);
		transactionRepo.save(fromTransaction);

		Transaction toTransaction = new Transaction();
		toTransaction.setType("Transfer (Credit)");
		toTransaction.setAmount(amount);
		toTransaction.setDate(LocalDateTime.now());
		toTransaction.setBankAccTransaction(toAccount);
		transactionRepo.save(toTransaction);

		bankAccountRepo.save(fromAccount);
		bankAccountRepo.save(toAccount);
	}

}
