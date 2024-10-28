package com.weddio.weddio.services.implementation;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.repositories.AccountRepository;
import com.weddio.weddio.services.implementation.base.BaseServiceImpl;
import com.weddio.weddio.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl extends BaseServiceImpl<Accounts, Long> implements AccountService {
	private AccountRepository accountRepository;
}
