package com.weddio.weddio.services.implementation;

import com.weddio.weddio.dto.responses.GuestFamilyResponse;
import com.weddio.weddio.dto.responses.GuestFriendResponse;
import com.weddio.weddio.dto.responses.GuestNeighborResponse;
import com.weddio.weddio.dto.responses.GuestResponse;
import com.weddio.weddio.models.*;
import com.weddio.weddio.models.enums.*;
import com.weddio.weddio.repositories.AccountRepository;
import com.weddio.weddio.repositories.GuestRepository;
import com.weddio.weddio.services.implementation.base.BaseServiceImpl;
import com.weddio.weddio.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends BaseServiceImpl<Accounts, Long> implements AccountService {

	private final ModelMapper modelMapper;
	private final AccountRepository accountRepository;
	private final GuestRepository guestRepository;

	private String getCellStringValue(Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		} else if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((int) cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}

	public Accounts getAccountByUsername(String username){
		return accountRepository.findByUsername (username).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Account is not found"));
	}

	public Object importGuests(Long accountId, MultipartFile file) throws Exception {
		Accounts account = findByIdFromRepo(accountId);

		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		List<Guest> importedGuests = new ArrayList<> ();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}

			String name = getCellStringValue(row.getCell(0));
			String gender = getCellStringValue(row.getCell(1)).trim();
			String whatsappNumber = getCellStringValue(row.getCell(2));
			String specialNickname = getCellStringValue(row.getCell(3));
			String address = getCellStringValue(row.getCell(4));
			String familyFrom = getCellStringValue(row.getCell(5));
			String friendType = getCellStringValue(row.getCell(6));
			String isNeighbor = getCellStringValue(row.getCell(7));

			Guest guest = new Guest();
			guest.setName(name);
			guest.setGender(Gender.valueOf(gender.toUpperCase()));
			guest.setWhatsappNumber(whatsappNumber);
			guest.setAttendenceStatus(AttendenceStatus.DRAFT);
			guest.setSpecialNickname(specialNickname);
			guest.setAddress(address);
			guest.setAccount(account);

			if (familyFrom != null) {
				try {
					Familys family = new Familys();
					family.setFamilyFrom(FamilyFrom.valueOf(familyFrom.trim().toUpperCase()));
					guest.setFamily(family);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Value family from is not valid, only mother or father.");
				}
			}

			if (friendType != null) {
				try {
					Friends friend = new Friends();
					friend.setFriendType(FriendType.valueOf(friendType.trim().toUpperCase()));
					guest.setFriend(friend);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Value friend type is not valid.");
				}
			}

			if (isNeighbor != null) {
				Neighbors neighbor = new Neighbors();
				guest.setNeighbor(neighbor);
			}

			guestRepository.save(guest);
			importedGuests.add(guest);
		}
		workbook.close();

		return importedGuests.stream().map(guest -> {
			if (guest.getFamily() != null) {
				return modelMapper.map(guest, GuestFamilyResponse.class);
			} else if (guest.getFriend() != null) {
				return modelMapper.map(guest, GuestFriendResponse.class);
			} else if (guest.getNeighbor() != null) {
				return modelMapper.map(guest, GuestNeighborResponse.class);
			} else {
				return modelMapper.map(guest, GuestResponse.class);
			}
		}).collect(Collectors.toList());
	}
}
