package com.Biblioteca.Repository.Producto;

import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngresoBajaProductoRepository extends JpaRepository<IngresoBajaProducto, Long> {

    /*
    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Producto> findAllByIdEmpresa(Long idEmpresa);*/
}
