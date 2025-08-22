package com.medilabosolutions.patient_service.mapper;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.model.Patient;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
//public interface PatientMapper {
//    @Mapping(target = "birthdate", source = "birthdate")
//    Patient toEntity(PatientCreateDTO dto);
//    Patient toEntity(PatientUpdateDTO dto);
//
//    PatientListItemDTO toPatientListItemDTO(Patient entity);
//    PatientCreateDTO toPatientCreateDTO(Patient entity);
//    PatientUpdateDTO toPatientUpdateDTO(Patient entity);
//}


@Component
public class PatientMapper {

    public Patient toEntity(PatientCreateDTO dto) {
        if (dto == null) return null;

        Patient patient = new Patient();
        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setBirthdate(dto.birthdate());
        patient.setGender(dto.gender());
        patient.setAddress(dto.address());
        patient.setPhone(dto.phone());

        return patient;
    }

    public Patient toEntity(Long id, PatientUpdateDTO dto) {
        if (dto == null) return null;

        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setBirthdate(dto.birthdate());
        patient.setGender(dto.gender());
        patient.setAddress(dto.address());
        patient.setPhone(dto.phone());

        return patient;
    }

    public PatientDTO toPatientDTO(Patient entity) {
        if (entity == null) return null;

        return new PatientDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthdate(),
                entity.getGender(),
                entity.getAddress(),
                entity.getPhone()
        );
    }

    public PatientCreateDTO toPatientCreateDTO(Patient entity) {
        if (entity == null) return null;

        return new PatientCreateDTO(
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthdate(),
                entity.getGender(),
                entity.getAddress(),
                entity.getPhone()
        );
    }

    public PatientUpdateDTO toPatientUpdateDTO(Patient entity) {
        if (entity == null) return null;

        return new PatientUpdateDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthdate(),
                entity.getGender(),
                entity.getAddress(),
                entity.getPhone()
        );
    }
}

