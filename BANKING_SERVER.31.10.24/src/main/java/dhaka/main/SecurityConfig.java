package dhaka.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(customizer -> customizer.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/user/**").permitAll()
						.requestMatchers("/user/balance/**", "/user/transactions/**").authenticated()
						.requestMatchers("/admin/**").hasRole("ADMIN"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(Customizer.withDefaults());

		return http.build();

		// .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
		// .logout(logout -> logout.permitAll());
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

		// Define the in-memory admin authentication
		authenticationManagerBuilder.inMemoryAuthentication().withUser("dhaka-admin")
				.password("dhaka_banking")// Admin password (can be encrypted if needed)
				.roles("ADMIN");

		return authenticationManagerBuilder.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

//	@Bean
//	public InMemoryUserDetailsManager userDetailsService() {
//		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("adminpassword"))
//				.roles("ADMIN").build();
//
//		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("userpassword")).roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(admin, user);
//	}
//

//	@Bean
//	public AuthenticationProvider authenticate(String username) {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//		provider.setUserDetailsService(userDetailsService);
//		return provider;
//	}