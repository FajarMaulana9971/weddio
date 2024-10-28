package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Neighbors;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborRepository extends BaseRepository<Neighbors, Long> {
}
