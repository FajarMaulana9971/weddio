package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<Accounts, Long> {
}
