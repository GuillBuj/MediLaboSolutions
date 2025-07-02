package com.medilabosolutions.patient_service.mapper;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientListItemDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "birthdate", source = "birthdate")
    Patient toEntity(PatientCreateDTO dto);
    Patient toEntity(PatientUpdateDTO dto);

    PatientListItemDTO toPatientListItemDTO(Patient entity);
    PatientCreateDTO toPatientCreateDTO(Patient entity);
    PatientUpdateDTO toPatientUpdateDTO(Patient entity);
}
