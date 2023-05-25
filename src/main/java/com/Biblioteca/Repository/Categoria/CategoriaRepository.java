package com.Biblioteca.Repository.Categoria;

import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query(value = "SELECT * FROM categoria WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Categoria> findAllByIdEmpresa(Long idEmpresa);

}
