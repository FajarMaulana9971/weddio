package com.weddio.weddio.controllers;

import com.weddio.weddio.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
	private AccountService accountService;

	@PostMapping("import-guest/{accountId}")
	public String importGuest(@PathVariable Long accountId, MultipartFile file) {
		try{
			accountService.importGuests (accountId, file);
			return "File successfully import";
		}catch (Exception e){
			return "Error: " + e.getMessage();
		}
	}
}
