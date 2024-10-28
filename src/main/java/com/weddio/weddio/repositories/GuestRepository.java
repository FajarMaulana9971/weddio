package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Guest;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends BaseRepository<Guest, Long> {
	@Query("SELECT g FROM Guest g WHERE g.account.id = :accountId")
	List<Guest> findByAccountId(@Param ("accountId") Long accountId);
}
