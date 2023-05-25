package com.Biblioteca.Repository.Producto.Baja;

import com.Biblioteca.Models.Producto.Baja.BajaProducto;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BajaProductoRepository extends JpaRepository<BajaProducto, Long> {

    /*
    @Query(value = "SELECT * FROM producto WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Producto> findAllByIdEmpresa(Long idEmpresa);*/
}
