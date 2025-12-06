package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.model.Medic;
import com.michelmaia.timecare_core.repository.MedicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicService {

    private final MedicRepository medicRepo;

    public MedicService(MedicRepository medicRepo) {
        this.medicRepo = medicRepo;
    }

    public List<Medic> getAllDoctors() {
        return medicRepo.findAll();
    }

    public Optional<Medic> getDoctorById(Long id) {
        return medicRepo.findById(id);
    }

    public Medic create(Medic medic) {
        return medicRepo.save(medic);
    }
}
