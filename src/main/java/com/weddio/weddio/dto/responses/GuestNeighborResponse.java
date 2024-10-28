package com.weddio.weddio.dto.responses;

import com.weddio.weddio.models.enums.FamilyFrom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestNeighborResponse {
	private Long id;

	private String firstName;

	private String lastName;

	private String whatsappNumber;

	private String address;

	private Long neighborId;

	private Long accountId;

}
