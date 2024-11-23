package com.weddio.weddio.services;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthUserDetailService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	public AuthUser loadUserByUsername(String username) {
		Accounts accounts = accountRepository.findByUsername (username)
				.orElseThrow(() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Account is not found"));

		return new AuthUser(accounts);
	}
}
