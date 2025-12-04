package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.dto.DoctorInputDTO;
import com.michelmaia.timecare_core.entity.Doctor;
import com.michelmaia.timecare_core.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;

    public DoctorService(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepo.findById(id);
    }

   public Doctor create(DoctorInputDTO doctorInput) {
       Doctor doctor = new Doctor();
       doctor.setName(doctorInput.name());
       doctor.setSpecialty(doctorInput.specialty());
       return doctorRepo.save(doctor);
    }
}
