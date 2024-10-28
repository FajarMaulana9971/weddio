package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Guest;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends BaseRepository<Guest, Long> {
}
