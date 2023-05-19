package com.Biblioteca.Repository.Persona;

import com.Biblioteca.Models.Persona.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByEmail(String email);

    Optional<Persona> findByCedula(String cedula);

    Boolean existsByCedula (String cedula);

}
