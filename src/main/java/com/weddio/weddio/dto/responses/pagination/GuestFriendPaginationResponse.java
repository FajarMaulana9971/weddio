package com.weddio.weddio.dto.responses.pagination;

import com.weddio.weddio.dto.responses.GuestFriendResponse;
import com.weddio.weddio.utils.PageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestFriendPaginationResponse {
	private PageData pageData;
	private List<GuestFriendResponse> guestFriendResponses;
}
