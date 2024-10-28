package com.weddio.weddio.dto.responses.pagination;

import com.weddio.weddio.dto.responses.GuestFamilyResponse;
import com.weddio.weddio.utils.PageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestFamilyPaginationResponse {
	private PageData pageData;
	private List<GuestFamilyResponse> guestFamily;
}
