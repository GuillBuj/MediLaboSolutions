package com.medilabosolutions.patient_service.service;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.exception.PatientNotFoundException;
import com.medilabosolutions.patient_service.mapper.PatientMapper;
import com.medilabosolutions.patient_service.model.Patient;
import com.medilabosolutions.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;
    private PatientDTO patientDTO1;
    private PatientDTO patientDTO2;
    private PatientUpdateDTO patientUpdateDTO;

    @BeforeEach
    void setUp() {
        patient1 = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.parse("1990-01-01"))
                .gender("M")
                .address("123 Main St")
                .phone("555-1234")
                .build();

        patient2 = Patient.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .birthdate(LocalDate.parse("1985-05-15"))
                .gender("F")
                .address("456 Oak Ave")
                .phone("555-5678")
                .build();

        patientDTO1 = new PatientDTO(1L, "John", "Doe", LocalDate.parse("1990-01-01"), "M", "123 Main St", "555-1234");
        patientDTO2 = new PatientDTO(2L, "Jane", "Smith", LocalDate.parse("1985-05-15"), "F", "456 Oak Ave", "555-5678");

        patientUpdateDTO = new PatientUpdateDTO(1L,"John", "Updated", LocalDate.parse("1990-01-01"), "M", "456 New St", "555-5678");
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatientDTO() {
        // Given
        PatientCreateDTO createDTO = new PatientCreateDTO("John", "Doe", LocalDate.parse("1990-01-01"), "M", "123 Main St", "555-1234");

        when(patientMapper.toEntity(createDTO)).thenReturn(patient1);
        when(patientRepository.save(patient1)).thenReturn(patient1);
        when(patientMapper.toPatientDTO(patient1)).thenReturn(patientDTO1);

        // When
        PatientDTO result = patientService.createPatient(createDTO);

        // Then
        assertThat(result).isEqualTo(patientDTO1);
        verify(patientRepository).save(patient1);
        verify(patientMapper).toEntity(createDTO);
        verify(patientMapper).toPatientDTO(patient1);
    }

    @Test
    void updatePatient_WhenPatientExists_ShouldUpdateAndReturnDTO() {
        // Given
        Long patientId = 1L;
        Patient updatedPatient = Patient.builder()
                .id(patientId)
                .firstName("John")
                .lastName("Updated")
                .birthdate(LocalDate.parse("1990-01-01"))
                .gender("M")
                .address("456 New St")
                .phone("555-5678")
                .build();

        PatientUpdateDTO expectedDTO = new PatientUpdateDTO(1L,"John", "Updated", LocalDate.parse("1990-01-01"), "M", "456 New St", "555-5678");

        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(patientMapper.toEntity(patientId, patientUpdateDTO)).thenReturn(updatedPatient);
        when(patientRepository.save(updatedPatient)).thenReturn(updatedPatient);
        when(patientMapper.toPatientUpdateDTO(updatedPatient)).thenReturn(expectedDTO);

        // When
        PatientUpdateDTO result = patientService.updatePatient(patientId, patientUpdateDTO);

        // Then
        assertThat(result).isEqualTo(expectedDTO);
        verify(patientRepository).existsById(patientId);
        verify(patientMapper).toEntity(patientId, patientUpdateDTO);
        verify(patientRepository).save(updatedPatient);
    }

    @Test
    void updatePatient_WhenPatientNotExists_ShouldThrowException() {
        // Given
        Long patientId = 999L;

        when(patientRepository.existsById(patientId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatient(patientId, patientUpdateDTO))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient non trouvé avec l'ID : 999");

        verify(patientRepository).existsById(patientId);
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldDeletePatient() {
        // Given
        Long patientId = 1L;
        when(patientRepository.existsById(patientId)).thenReturn(true);

        // When
        patientService.deletePatient(patientId);

        // Then
        verify(patientRepository).existsById(patientId);
        verify(patientRepository).deleteById(patientId);
    }

    @Test
    void deletePatient_WhenPatientNotExists_ShouldThrowException() {
        // Given
        Long patientId = 999L;
        when(patientRepository.existsById(patientId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> patientService.deletePatient(patientId))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient non trouvé avec l'ID : 999");

        verify(patientRepository).existsById(patientId);
        verify(patientRepository, never()).deleteById(any());
    }

    @Test
    void getPatientById_WhenPatientExists_ShouldReturnPatientDTO() {
        // Given
        Long patientId = 1L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient1));
        when(patientMapper.toPatientDTO(patient1)).thenReturn(patientDTO1);

        // When
        PatientDTO result = patientService.getPatientById(patientId);

        // Then
        assertThat(result).isEqualTo(patientDTO1);
        verify(patientRepository).findById(patientId);
        verify(patientMapper).toPatientDTO(patient1);
    }

    @Test
    void getPatientById_WhenPatientNotExists_ShouldThrowException() {
        // Given
        Long patientId = 999L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.getPatientById(patientId))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient non trouvé avec l'ID : 999");

        verify(patientRepository).findById(patientId);
        verify(patientMapper, never()).toPatientDTO(any());
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatientDTOs() {
        // Given
        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2));
        when(patientMapper.toPatientDTO(patient1)).thenReturn(patientDTO1);
        when(patientMapper.toPatientDTO(patient2)).thenReturn(patientDTO2);

        // When
        List<PatientDTO> result = patientService.getAllPatients();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(patientDTO1, patientDTO2);
        verify(patientRepository).findAll();
        verify(patientMapper, times(2)).toPatientDTO(any());
    }

    @Test
    void getAllPatients_WhenNoPatients_ShouldReturnEmptyList() {
        // Given
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<PatientDTO> result = patientService.getAllPatients();

        // Then
        assertThat(result).isEmpty();
        verify(patientRepository).findAll();
        verify(patientMapper, never()).toPatientDTO(any());
    }

    @Test
    void deleteAllPatients_ShouldCallRepositoryDeleteAll() {
        // When
        patientService.deleteAllPatients();

        // Then
        verify(patientRepository).deleteAll();
    }

    @Test
    void getPatientById_WithDifferentPatient_ShouldReturnCorrectPatientDTO() {
        // Given
        Long patientId = 2L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient2));
        when(patientMapper.toPatientDTO(patient2)).thenReturn(patientDTO2);

        // When
        PatientDTO result = patientService.getPatientById(patientId);

        // Then
        assertThat(result).isEqualTo(patientDTO2);
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.firstName()).isEqualTo("Jane");
        assertThat(result.lastName()).isEqualTo("Smith");
        verify(patientRepository).findById(patientId);
        verify(patientMapper).toPatientDTO(patient2);
    }
}
