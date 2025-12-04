package com.michelmaia.timecare_core.repository;

import com.michelmaia.timecare_core.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
