package dhaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhaka.entity.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
	
}
