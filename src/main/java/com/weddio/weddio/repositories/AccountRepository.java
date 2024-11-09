package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Accounts, Long> {
	Optional<Accounts> findByUsername(@Param ("username") String username);
}
