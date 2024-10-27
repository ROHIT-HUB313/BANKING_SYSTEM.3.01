package dhaka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dhaka.entity.Users;
import dhaka.service.AdminService;

@RestController
@RequestMapping("/admin")
public class BankAdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users/!")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
    
    @GetMapping("/user/{name}")
    public ResponseEntity<Users> getUser(@PathVariable("name") String userName){
    	return ResponseEntity.ok(adminService.getUserOnly(userName));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody Users updateUser) {
        adminService.updateUserDetails(userId, updateUser);
        return ResponseEntity.ok("User details updated successfully");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountNo, @RequestParam double amount) {
        adminService.deposit(accountNo, amount);
        return ResponseEntity.ok("Deposit Successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountNo, @RequestParam double amount) {
        adminService.withdraw(accountNo, amount);
        return ResponseEntity.ok("Withdraw Successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountNo, @RequestParam Long toAccountNo, @RequestParam double amount) {
        adminService.transfer(fromAccountNo, toAccountNo, amount);
        return ResponseEntity.ok("Transfer Successful");
    }
}

