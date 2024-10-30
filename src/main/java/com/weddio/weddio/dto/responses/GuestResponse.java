package com.weddio.weddio.dto.responses;

import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import com.weddio.weddio.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestResponse {
	private Long id;

	private String name;

	private String whatsappNumber;

	private String address;

	private Gender gender;

	private Long accountId;

	private Long familyId;

	private FamilyFrom familyFrom;

	private Long friendId;

	private FriendType friendType;

	private Long neighborId;
}
