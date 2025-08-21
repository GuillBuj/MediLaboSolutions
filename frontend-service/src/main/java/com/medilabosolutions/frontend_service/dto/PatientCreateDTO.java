package com.medilabosolutions.frontend_service.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreateDTO{
        @NotBlank(message = "Le prénom est obligatoire")
        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
        String firstName;

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String lastName;

        @NotNull(message = "La date de naissance est obligatoire")
        @Past(message = "La date de naissance doit être dans le passé")
        LocalDate birthdate;

        @NotBlank(message = "Le genre est obligatoire")
        @Pattern(regexp = "^(?i)(M|F)$", message = "Le genre doit être 'M' ou 'F'")
        String gender;

        @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
        String address;

        @Pattern(
                regexp = "^\\+?[0-9. ()-]{7,25}$",
                message = "Le numéro de téléphone est invalide"
        )
        String phone;
}