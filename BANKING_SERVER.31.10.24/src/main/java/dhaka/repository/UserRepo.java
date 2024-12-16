package dhaka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhaka.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Long>{
	Optional<Users> findByEmail(String email);
	Users findByName(String name);//for userdetails or security
}
