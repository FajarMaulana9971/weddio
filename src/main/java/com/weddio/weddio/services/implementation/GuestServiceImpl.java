package com.weddio.weddio.services.implementation;

import com.weddio.weddio.dto.responses.GuestFamilyResponse;
import com.weddio.weddio.dto.responses.GuestFriendResponse;
import com.weddio.weddio.dto.responses.GuestNeighborResponse;
import com.weddio.weddio.dto.responses.pagination.GuestFamilyPaginationResponse;
import com.weddio.weddio.dto.responses.pagination.GuestFriendPaginationResponse;
import com.weddio.weddio.dto.responses.pagination.GuestNeighborPagination;
import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.models.Guest;
import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import com.weddio.weddio.models.enums.SearchType;
import com.weddio.weddio.repositories.GuestRepository;
import com.weddio.weddio.services.implementation.base.BaseServiceImpl;
import com.weddio.weddio.services.interfaces.AccountService;
import com.weddio.weddio.services.interfaces.GuestService;
import com.weddio.weddio.services.interfaces.base.BaseService;
import com.weddio.weddio.specifications.GuestSpecification;
import com.weddio.weddio.utils.PageData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BaseServiceImpl<Guest, Long> implements GuestService {

	private final GuestRepository guestRepository;

	private final ModelMapper modelMapper;

	public Object getGuestById(Long id) {
		Guest guest = findByIdFromRepo (id);

		if(guest.getFamily () != null) {
			GuestFamilyResponse guestFamilyResponse = modelMapper.map (guest, GuestFamilyResponse.class);
			guestFamilyResponse.setFamilyFrom (guest.getFamily ().getFamilyFrom ());
			guestFamilyResponse.setFamilyId (guest.getFamily ().getId ());
			guestFamilyResponse.setAccountId (guest.getAccount ().getId ());
			return guestFamilyResponse;
		}else if(guest.getFriend () != null) {
			GuestFriendResponse guestFriendResponse = modelMapper.map (guest, GuestFriendResponse.class);
			guestFriendResponse.setFriendType (guest.getFriend ().getFriendType ());
			guestFriendResponse.setFriendId (guest.getFriend ().getId ());
			guestFriendResponse.setAccountId (guest.getAccount ().getId ());
			return guestFriendResponse;
		}else if(guest.getNeighbor () != null) {
			GuestNeighborResponse guestNeighborResponse = modelMapper.map (guest, GuestNeighborResponse.class);
			guestNeighborResponse.setNeighborId (guest.getNeighbor ().getId ());
			guestNeighborResponse.setAccountId (guest.getAccount ().getId ());
			return guestNeighborResponse;
		}

		return new ResponseStatusException (HttpStatus.BAD_REQUEST, "The guest has not associated with family, neighbor, or friends");
	}

	public Object getGuestByAccountId(Long accountId) {
		List<Guest> guests = guestRepository.findByAccountId(accountId);
		return guests.stream().map(guest -> {
			if (guest.getFamily() != null) {
				GuestFamilyResponse response = modelMapper.map(guest, GuestFamilyResponse.class);
				response.setFamilyFrom(guest.getFamily().getFamilyFrom());
				response.setFamilyId (guest.getFamily ().getId ());
				response.setAccountId(guest.getAccount().getId());
				return response;
			} else if (guest.getFriend() != null) {
				GuestFriendResponse response = modelMapper.map(guest, GuestFriendResponse.class);
				response.setFriendType(guest.getFriend().getFriendType());
				response.setFriendId (guest.getFriend ().getId ());
				response.setAccountId(guest.getAccount().getId());
				return response;
			} else if (guest.getNeighbor() != null) {
				GuestNeighborResponse response = modelMapper.map(guest, GuestNeighborResponse.class);
				response.setNeighborId (guest.getNeighbor ().getId ());
				response.setAccountId(guest.getAccount().getId());
				return response;
			} else {
				throw new ResponseStatusException (HttpStatus.NOT_FOUND, "Guest has no related family, friend, or neighbor");
			}
		}).collect(Collectors.toList());
	}

	public Object getAllGuestByFilter(
			String firstName,
			String lastName,
			FamilyFrom familyFrom,
			FriendType friendType,
			SearchType searchType,
			int currentPage,
			int pageSize,
			UriComponentsBuilder uriComponentsBuilder
	){
		Specification<Guest> spec = Specification.where (null);

		if(firstName != null){
			spec = spec.and (GuestSpecification.hasFirstNameLike (firstName));
		}
		if(lastName != null){
			spec = spec.and (GuestSpecification.hasLastNameLike (lastName));
		}
		if(familyFrom != null){
			spec = spec.and (GuestSpecification.hasFamilyFrom (familyFrom));
		}
		if(friendType != null){
			spec = spec.and (GuestSpecification.hasFriendType (friendType));
		}

		switch (searchType){
			case FAMILY :
				spec = spec.and (GuestSpecification.isFamily ());
				break;
			case FRIEND:
				spec = spec.and (GuestSpecification.isFriend ());
				break;
			case NEIGHBOR:
				spec = spec.and (GuestSpecification.isNeighbor ());
				break;
			default:
				throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "Search type not supported");
		}

		Page<Guest> guestPage = guestRepository.findAll(spec, PageRequest.of(currentPage, pageSize));
		PageData pageData = PageData.pagination (guestPage.getTotalElements (), currentPage, pageSize, uriComponentsBuilder);

		switch (searchType){
			case FAMILY :
				List<GuestFamilyResponse> familyResponses = guestPage.getContent ().stream ().map (guest -> modelMapper.map (guest, GuestFamilyResponse.class)).collect(Collectors.toList());
				return new GuestFamilyPaginationResponse (pageData, familyResponses);
			case FRIEND:
				List<GuestFriendResponse> friendResponses = guestPage.getContent ().stream ().map (guest -> modelMapper.map (guest, GuestFriendResponse.class)).collect(Collectors.toList());
				return new GuestFriendPaginationResponse (pageData, friendResponses);
			case NEIGHBOR:
				List<GuestNeighborResponse>neighborResponses = guestPage.getContent ().stream ().map (guest -> modelMapper.map (guest, GuestNeighborResponse.class)).collect(Collectors.toList());
				return new GuestNeighborPagination (pageData, neighborResponses);
			default:
				throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "Search type not supported");
		}

	}
}
