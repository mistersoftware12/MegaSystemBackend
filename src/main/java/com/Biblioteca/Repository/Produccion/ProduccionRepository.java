package com.Biblioteca.Repository.Produccion;

import com.Biblioteca.Models.Produccion.Produccion;
import com.Biblioteca.Models.Producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProduccionRepository extends JpaRepository<Produccion, Long> {


    @Query(value = "SELECT * FROM produccion WHERE  empresa_id = :idEmpresa ORDER BY nombre ASC ", nativeQuery = true)
    List<Produccion> findAllByIdEmpresa(Long idEmpresa);

    @Query(value = "SELECT * FROM produccion WHERE  empresa_id = :idEmpresa AND UPPER(nombre) LIKE UPPER (:aguja) ORDER BY nombre ASC", nativeQuery = true)
    List<Produccion> findAllByIdEmpresaAguja(Long idEmpresa , String aguja);

    @Query(value = "SELECT * FROM produccion WHERE  empresa_id = :idEmpresa AND codigo_barra =:codigoBarra", nativeQuery = true)
    Optional<Produccion> findAllByIdCodigoEmpresa(Long idEmpresa, String codigoBarra);
}
