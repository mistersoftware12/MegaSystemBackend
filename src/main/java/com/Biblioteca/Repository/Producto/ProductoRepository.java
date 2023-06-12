package com.Biblioteca.Repository.Producto;

import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa ORDER BY nombre ASC", nativeQuery = true)
    List<Producto> findAllByIdEmpresa(Long idEmpresa);

    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa AND codigo_barra =:codigoBarra", nativeQuery = true)
    Optional<Producto> findAllByIdCodigoEmpresa(Long idEmpresa, String codigoBarra);
}
