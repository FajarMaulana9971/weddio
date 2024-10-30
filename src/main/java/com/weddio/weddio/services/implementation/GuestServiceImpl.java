package com.weddio.weddio.services.implementation;

import com.weddio.weddio.dto.responses.GuestFamilyResponse;
import com.weddio.weddio.dto.responses.GuestFriendResponse;
import com.weddio.weddio.dto.responses.GuestNeighborResponse;
import com.weddio.weddio.dto.responses.GuestResponse;
import com.weddio.weddio.dto.responses.pagination.GuestFamilyPaginationResponse;
import com.weddio.weddio.dto.responses.pagination.GuestFriendPaginationResponse;
import com.weddio.weddio.dto.responses.pagination.GuestNeighborPagination;
import com.weddio.weddio.dto.responses.pagination.GuestResponsePagination;
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
			String name,
			FamilyFrom familyFrom,
			FriendType friendType,
			SearchType searchType,
			int currentPage,
			int pageSize,
			UriComponentsBuilder uriComponentsBuilder
	) {
		Specification<Guest> spec = Specification.where(null);

		if (name != null) {
			spec = spec.and(GuestSpecification.hasNameLike(name));
		}
		if (familyFrom != null) {
			spec = spec.and(GuestSpecification.hasFamilyFrom(familyFrom));
		}
		if (friendType != null) {
			spec = spec.and(GuestSpecification.hasFriendType(friendType));
		}

		if (searchType != null) {
			switch (searchType) {
				case FAMILY:
					spec = spec.and(GuestSpecification.isFamily());
					Page<Guest> familyPage = guestRepository.findAll(spec, PageRequest.of(currentPage, pageSize));
					PageData familyPageData = PageData.pagination(familyPage.getTotalElements(), currentPage, pageSize, uriComponentsBuilder);

					List<GuestFamilyResponse> familyResponses = familyPage.getContent().stream()
							.map(guest -> {
								GuestFamilyResponse response = modelMapper.map(guest, GuestFamilyResponse.class);
								response.setFamilyFrom(guest.getFamily().getFamilyFrom());
								response.setFamilyId(guest.getFamily().getId());
								response.setAccountId(guest.getAccount().getId());
								return response;
							})
							.collect(Collectors.toList());

					return new GuestFamilyPaginationResponse(familyPageData, familyResponses);

				case FRIEND:
					spec = spec.and(GuestSpecification.isFriend());
					Page<Guest> friendPage = guestRepository.findAll(spec, PageRequest.of(currentPage, pageSize));
					PageData friendPageData = PageData.pagination(friendPage.getTotalElements(), currentPage, pageSize, uriComponentsBuilder);

					List<GuestFriendResponse> friendResponses = friendPage.getContent().stream()
							.map(guest -> {
								GuestFriendResponse response = modelMapper.map(guest, GuestFriendResponse.class);
								response.setFriendType(guest.getFriend().getFriendType());
								response.setFriendId(guest.getFriend().getId());
								response.setAccountId(guest.getAccount().getId());
								return response;
							})
							.collect(Collectors.toList());

					return new GuestFriendPaginationResponse(friendPageData, friendResponses);

				case NEIGHBOR:
					spec = spec.and(GuestSpecification.isNeighbor());
					Page<Guest> neighborPage = guestRepository.findAll(spec, PageRequest.of(currentPage, pageSize));
					PageData neighborPageData = PageData.pagination(neighborPage.getTotalElements(), currentPage, pageSize, uriComponentsBuilder);

					List<GuestNeighborResponse> neighborResponses = neighborPage.getContent().stream()
							.map(guest -> {
								GuestNeighborResponse response = modelMapper.map(guest, GuestNeighborResponse.class);
								response.setNeighborId(guest.getNeighbor().getId());
								response.setAccountId(guest.getAccount().getId());
								return response;
							})
							.collect(Collectors.toList());

					return new GuestNeighborPagination(neighborPageData, neighborResponses);

				default:
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search type not supported");
			}
		}

		Page<Guest> guestPage = guestRepository.findAll(spec, PageRequest.of(currentPage, pageSize));
		PageData pageData = PageData.pagination(guestPage.getTotalElements(), currentPage, pageSize, uriComponentsBuilder);

		List<GuestResponse> guestResponses = guestPage.getContent().stream()
				.map(guest -> {
					GuestResponse response = modelMapper.map(guest, GuestResponse.class);
					response.setFamilyId(guest.getFamily() != null ? guest.getFamily().getId() : null);
					response.setFamilyFrom(guest.getFamily() != null ? guest.getFamily().getFamilyFrom() : null);
					response.setFriendId(guest.getFriend() != null ? guest.getFriend().getId() : null);
					response.setFriendType(guest.getFriend() != null ? guest.getFriend().getFriendType() : null);
					response.setNeighborId(guest.getNeighbor() != null ? guest.getNeighbor().getId() : null);
					return response;
				})
				.collect(Collectors.toList());

		return new GuestResponsePagination (pageData, guestResponses);
	}

}
