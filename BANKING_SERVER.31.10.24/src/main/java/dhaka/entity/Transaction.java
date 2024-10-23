package dhaka.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	private String type;
	private double amount;
	private LocalDateTime date;

	@ManyToOne
	@JoinColumn(name = "bank_account_id")
	private BankAccount bankAccTransaction;

	public Transaction(Long transactionId, String type, double amount, LocalDateTime date, BankAccount bankAccount) {
		this.transactionId = transactionId;
		this.type = type;
		this.amount = amount;
		this.date = date;
		this.bankAccTransaction = bankAccount;
	}
}

