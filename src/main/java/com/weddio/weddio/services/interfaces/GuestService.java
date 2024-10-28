package com.weddio.weddio.services.interfaces;

import com.weddio.weddio.models.Guest;
import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import com.weddio.weddio.models.enums.SearchType;
import com.weddio.weddio.services.interfaces.base.BaseService;
import org.springframework.web.util.UriComponentsBuilder;

public interface GuestService extends BaseService<Guest, Long> {
	Object getGuestById(Long id);

	Object getGuestByAccountId(Long accountId);

	Object getAllGuestByFilter(
			String firstName,
			String lastName,
			FamilyFrom familyFrom,
			FriendType friendType,
			SearchType searchType,
			int currentPage,
			int pageSize,
			UriComponentsBuilder uriComponentsBuilder
	);

}
