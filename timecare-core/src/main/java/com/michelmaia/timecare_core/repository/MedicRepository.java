package com.michelmaia.timecare_core.repository;

import com.michelmaia.timecare_core.model.Medic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicRepository extends JpaRepository<Medic, Long> {
}
