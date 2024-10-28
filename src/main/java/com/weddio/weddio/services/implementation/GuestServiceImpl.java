package com.weddio.weddio.services.implementation;

import com.weddio.weddio.models.Guest;
import com.weddio.weddio.repositories.GuestRepository;
import com.weddio.weddio.services.implementation.base.BaseServiceImpl;
import com.weddio.weddio.services.interfaces.GuestService;
import com.weddio.weddio.services.interfaces.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BaseServiceImpl<Guest, Long> implements GuestService {
	private final GuestRepository guestRepository;

}
