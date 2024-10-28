package com.weddio.weddio.repositories;

import com.weddio.weddio.models.Familys;
import com.weddio.weddio.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends BaseRepository<Familys, Long> {
}
