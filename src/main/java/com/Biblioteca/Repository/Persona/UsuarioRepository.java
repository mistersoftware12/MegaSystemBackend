package com.Biblioteca.Repository.Persona;

import com.Biblioteca.Models.Persona.Persona;
import com.Biblioteca.Models.Persona.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM usuario u INNER JOIN persona p ON u.persona_id = p.id WHERE empresa_id = :idEmpresa", nativeQuery = true)
    List<Usuario> findAllByIdEmpresa(Long idEmpresa);

    @Query(value = "SELECT * FROM usuario u INNER JOIN persona p ON u.persona_id = p.id WHERE p.empresa_id = :idEmpresa AND u.id =:idUsuario", nativeQuery = true)
    Optional<Usuario> findByIdIdEmpresa(Long idEmpresa , Long idUsuario);

    @Query(value = "SELECT * FROM usuario u INNER JOIN persona p ON u.persona_id = p.id WHERE p.empresa_id = :idEmpresa AND p.cedula =:cedulaUsuario", nativeQuery = true)
    Optional<Usuario> findByIdCedulaEmpresa(Long idEmpresa , String cedulaUsuario);

    @Query(value = "SELECT * FROM usuario u INNER JOIN persona p ON u.persona_id = p.id WHERE  p.cedula =:cedulaUsuario", nativeQuery = true)
    Optional<Usuario> findByCedula(String cedulaUsuario);

    Optional<Usuario> findByPersona(Persona persona);
}
