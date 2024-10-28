package com.weddio.weddio.controllers;

import com.weddio.weddio.services.interfaces.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/guests")
public class GuestController {

	private GuestService guestService;

	@GetMapping("/{id}")
	public Object getGuestById(@PathVariable Long id) {
		return guestService.getGuestById (id);
	}

	@GetMapping("byAccount/{accountId}")
	public Object getGuestByAccountId(@PathVariable Long accountId) {
		return guestService.getGuestByAccountId(accountId);
	}
}
