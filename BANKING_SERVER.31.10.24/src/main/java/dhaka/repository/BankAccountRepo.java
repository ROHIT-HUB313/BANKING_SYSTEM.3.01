package dhaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhaka.entity.BankAccount;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long>{

}
