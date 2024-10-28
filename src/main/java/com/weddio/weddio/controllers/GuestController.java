package com.weddio.weddio.controllers;

import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import com.weddio.weddio.models.enums.SearchType;
import com.weddio.weddio.services.interfaces.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

	@GetMapping("/filter")
	public Object getAllGuestByFilter(
			@RequestParam (required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) FamilyFrom familyFrom,
			@RequestParam(required = false) FriendType friendType,
			@RequestParam SearchType searchType,
			@RequestParam(defaultValue = "0") int currentPage,
			@RequestParam(defaultValue = "10") int pageSize,
			UriComponentsBuilder uriBuilder
	) {
		return guestService.getAllGuestByFilter(
				firstName, lastName, familyFrom, friendType, searchType, currentPage, pageSize, uriBuilder
		);
	}
}
