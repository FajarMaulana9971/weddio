package com.weddio.weddio.dto.responses;

import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestNeighborResponse {
	private Long id;

	private String name;

	private String whatsappNumber;

	private String address;

	private Gender gender;

	private Long neighborId;

	private Long accountId;

}
