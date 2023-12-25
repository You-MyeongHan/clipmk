package com.clipmk.region.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clipmk.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer>{
	Optional<Region> findByCode(String code);
}
