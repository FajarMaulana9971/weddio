package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Friends;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends BaseRepository<Friends, Long> {
}
