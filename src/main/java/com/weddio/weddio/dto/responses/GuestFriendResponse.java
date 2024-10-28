package com.weddio.weddio.dto.responses;

import com.weddio.weddio.models.enums.FriendType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestFriendResponse {
	private Long id;

	private String firstName;

	private String lastName;

	private String whatsappNumber;

	private String address;

	private FriendType friendType;

	private Long friendId;

	private Long accountId;

}
