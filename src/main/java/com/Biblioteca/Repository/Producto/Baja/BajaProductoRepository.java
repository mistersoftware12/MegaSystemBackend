package com.Biblioteca.Repository.Producto.Baja;

import com.Biblioteca.Models.Producto.Baja.BajaProducto;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BajaProductoRepository extends JpaRepository<BajaProducto, Long> {

    @Query(value = "SELECT * FROM producto_baja i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =:idEmpresa  AND fecha_registro>=:fechaInicio AND fecha_registro<=:fechaFin", nativeQuery = true)
    List<BajaProducto> findAllReporte1(Long idEmpresa, Date fechaInicio , Date fechaFin);


    @Query(value = "SELECT * FROM producto_baja i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =:idEmpresa AND P.usuario_id =:idUsuario  AND fecha_registro>=:fechaInicio AND fecha_registro<=:fechaFin", nativeQuery = true)
    List<BajaProducto> findAllReporteUsuario1(Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin);

}
