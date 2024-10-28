package com.weddio.weddio.repositories.base;

import com.weddio.weddio.models.base.BaseEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface BaseRepository <E extends BaseEntity, T> extends JpaRepository<E, T> {
}
