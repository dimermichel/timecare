package com.michelmaia.timecare_core.repository;

import com.michelmaia.timecare_core.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
}
