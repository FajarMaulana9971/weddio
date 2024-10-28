package com.weddio.weddio.dto.responses;

import com.weddio.weddio.models.enums.FamilyFrom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestFamilyResponse {
	private Long id;

	private String firstName;

	private String lastName;

	private String whatsappNumber;

	private String address;

	private FamilyFrom familyFrom;

	private Long familyId;

	private Long accountId;
}
