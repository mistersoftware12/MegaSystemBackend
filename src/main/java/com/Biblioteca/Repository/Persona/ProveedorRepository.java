package com.Biblioteca.Repository.Persona;


import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    @Query(value = "SELECT * FROM proveedor where empresa_id = :idEmpresa", nativeQuery = true)
    List<Proveedor> findAllByIdEmpresa(Long idEmpresa);

}
