package com.Biblioteca.Repository.Producto.Ingreso;

import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngresoProductoRepository extends JpaRepository<IngresoProducto, Long> {

    /*
    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Producto> findAllByIdEmpresa(Long idEmpresa);*/
}
