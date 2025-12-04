package com.michelmaia.timecare_core.repository;

import com.michelmaia.timecare_core.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
