package com.weddio.weddio.services.implementation.base;

import com.weddio.weddio.models.base.BaseEntity;
import com.weddio.weddio.repositories.base.BaseRepository;
import com.weddio.weddio.services.interfaces.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Primary
public class BaseServiceImpl<E extends BaseEntity, T> implements BaseService<E, T> {
	@Autowired
	private BaseRepository<E, T> baseRepository;

	@Override
	public E findByIdFromRepo(T id){
		return baseRepository.findById (id).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND));
	}
}
