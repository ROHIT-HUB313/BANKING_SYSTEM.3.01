package dhaka.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dhaka.filter.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /*UsernamePasswordAuthenticationToken, it doesn't authenticates the request, but it simply wraps up the credentials
      (email and password) for spring security to process.

     At this point, the token is unauthenticated.
     This is like preparing an ID card with your details but without any official validation yet(official validation is
     authenticate method of authentication manager).

     Behind the scenes, AuthenticationManager delegates the work to an AuthenticationProvider
     (like DaoAuthenticationProvider by default).*/


    /*authenticationManager -> daoAuthenticationProvider -> inMemoryUserDetailsManager or MyUserDetailsService.
    UsernamePasswordAuthenticationToken is only wrapping credentials for spring security to authenticate*/

    /*AuthenticationManager is often implemented by ProviderManager, which delegates the authentication process to one
    or more AuthenticationProvider instances. Default one is DaoAuthenticationProvider.
    question is can and how we use another provider by code implementation.*/

    /*authenticationManager.authenticate() returns a new instance of UsernamePasswordAuthenticationToken (not the same
     object passed in)
     prior token contains only username, password, authentication == false;
     later token contains roles and authorities, authentication == true; it is a new object.*/

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestParam("username") String username, @RequestParam("password") String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        // Ensure the user has admin role
        if (!role.equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access Denied"));
        }

        String token = jwtUtils.generateToken(userDetails.getUsername(), role);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/!")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<Users> getUser(@PathVariable("name") String userName) {
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

