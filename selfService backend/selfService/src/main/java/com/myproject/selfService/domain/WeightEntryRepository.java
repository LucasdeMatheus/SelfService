package com.myproject.selfService.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightEntryRepository extends JpaRepository<WeightEntry, Long> {
}
