package dhaka.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account--id")
	@SequenceGenerator(name = "account--id", sequenceName = "account--id", initialValue = 983241, allocationSize = 1)
	private Long accountNO;

	private double balance;

	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "user_id")
	private Users user;

	@OneToMany(mappedBy = "bankAccTransaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaction> transactions = new ArrayList<>();

	// transactions removed from constructor
	public BankAccount(double balance, Users user) {
		this.balance = balance;
		this.user = user;
	}
}
