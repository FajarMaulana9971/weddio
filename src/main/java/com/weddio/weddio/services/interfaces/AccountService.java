package com.weddio.weddio.services.interfaces;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.services.interfaces.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService extends BaseService<Accounts, Long> {
	Object importGuests(Long accountId, MultipartFile file) throws Exception;
}
