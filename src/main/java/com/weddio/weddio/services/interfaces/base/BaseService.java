package com.weddio.weddio.services.interfaces.base;

import com.weddio.weddio.models.base.BaseEntity;

public interface BaseService <E extends BaseEntity, T>{
	E findByIdFromRepo(T id);
}
