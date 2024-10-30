package com.weddio.weddio.dto.responses.pagination;

import com.weddio.weddio.dto.responses.GuestResponse;
import com.weddio.weddio.utils.PageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestResponsePagination {
	private PageData pageData;
	private List<GuestResponse> guestResponses;
}
