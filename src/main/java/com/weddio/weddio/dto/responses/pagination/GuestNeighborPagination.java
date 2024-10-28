package com.weddio.weddio.dto.responses.pagination;

import com.weddio.weddio.dto.responses.GuestNeighborResponse;
import com.weddio.weddio.utils.PageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestNeighborPagination {
	private PageData pageData;
	private List<GuestNeighborResponse> guestNeighborResponses;
}
