package com.Biblioteca.Repository.Producto;

import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Producto> findAllByIdEmpresa(Long idEmpresa);
}
