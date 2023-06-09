package com.Biblioteca.Repository.Producto.Venta;


import com.Biblioteca.Models.Venta.VentaContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface VentaContenidoRepository extends JpaRepository<VentaContenido, Long> {


    @Query(value = "SELECT * FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =:idEmpresa AND fecha_emision >=:fechaInicio AND fecha_emision <=:fechaFin", nativeQuery = true)
    List<VentaContenido> findAllReporte1(Long idEmpresa, Date fechaInicio , Date fechaFin);

    @Query(value = "SELECT * FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =:idEmpresa AND e.usuario_id =:idUsuario  AND  fecha_emision >=:fechaInicio AND fecha_emision <=:fechaFin", nativeQuery = true)
    List<VentaContenido> findAllReporteUsuario1(Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin);

}
