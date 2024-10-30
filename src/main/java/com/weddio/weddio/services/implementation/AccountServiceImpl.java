package com.weddio.weddio.services.implementation;

import com.weddio.weddio.models.*;
import com.weddio.weddio.models.enums.AttendenceStatus;
import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import com.weddio.weddio.models.enums.Gender;
import com.weddio.weddio.repositories.AccountRepository;
import com.weddio.weddio.repositories.GuestRepository;
import com.weddio.weddio.services.implementation.base.BaseServiceImpl;
import com.weddio.weddio.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AccountServiceImpl extends BaseServiceImpl<Accounts, Long> implements AccountService {
	private AccountRepository accountRepository;
	private GuestRepository guestRepository;

	private String getCellStringValue(Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		} else if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((int) cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}

	public void importGuests(Long accountId, MultipartFile file) throws Exception {
		Accounts account = findByIdFromRepo(accountId);

		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String name = getCellStringValue(row.getCell(0));
			String gender = getCellStringValue (row.getCell(1)).trim ();
			String whatsappNumber = getCellStringValue(row.getCell(2));
			String address = getCellStringValue(row.getCell(3));

			String familyFrom = getCellStringValue(row.getCell(4));
			String friendType = getCellStringValue(row.getCell(5));
			String isNeighbor = getCellStringValue(row.getCell(6));

			Guest guest = new Guest();
			guest.setName (name);
			guest.setGender (Gender.valueOf (gender.toUpperCase ()));
			guest.setWhatsappNumber(whatsappNumber);
			guest.setAttendenceStatus (AttendenceStatus.DRAFT);
			guest.setAddress(address);
			guest.setAccount(account);

			if (familyFrom != null) {
				String familyFromTrimmed = familyFrom.trim();
				try {
					Familys family = new Familys();
					family.setFamilyFrom(FamilyFrom.valueOf(familyFromTrimmed.toUpperCase()));
					guest.setFamily(family);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Value family from is not valid, only mother or father.");
				}
			}

			if (friendType != null) {
				String friendTypeTrimmed = friendType.trim();
				try {
					Friends friend = new Friends();
					friend.setFriendType(FriendType.valueOf(friendTypeTrimmed.toUpperCase()));
					guest.setFriend(friend);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Value friend type from is not valid");
				}
			}

			if (isNeighbor != null) {
				Neighbors neighbor = new Neighbors();
				guest.setNeighbor(neighbor);
			}
			guestRepository.save(guest);
		}
		workbook.close();
	}

}
