package dhaka.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user--id")
	@SequenceGenerator(name = "user--id", sequenceName = "user--id", initialValue = 1001, allocationSize = 1)
	private Long id;

	private String name;
	private String fatherName;
	private String email;
	private String mobileNo;
	private Integer age;
	private String password;
	private String role;

	@JsonManagedReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private BankAccount bankAccount;

	public Users(String name, String fatherName, String email, String mobileNo, Integer age, String password) {
		this.name = name;
		this.fatherName = fatherName;
		this.email = email;
		this.mobileNo = mobileNo;
		this.age = age;
		this.password = password;
	}
}
