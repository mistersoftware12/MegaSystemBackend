package com.Biblioteca.Repository.Persona;

import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Persona;
import com.Biblioteca.Models.Persona.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByPersona (Persona persona);

    @Query(value = "SELECT * FROM cliente u INNER JOIN persona p ON u.persona_id = p.id WHERE empresa_id = :idEmpresa", nativeQuery = true)
    List<Cliente> findAllByIdEmpresa(Long idEmpresa);

}
