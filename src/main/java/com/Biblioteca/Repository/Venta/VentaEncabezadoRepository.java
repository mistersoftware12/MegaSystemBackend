package com.Biblioteca.Repository.Venta;


import com.Biblioteca.Models.Venta.VentaEncabezado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaEncabezadoRepository extends JpaRepository<VentaEncabezado, Long> {


    @Query(value = "SELECT * FROM venta_encabezado WHERE  empresa_id =:idEmpresa AND extract(month from  fecha_emision) =:mes AND extract(year from fecha_emision) =:anio ORDER BY secuencia DESC", nativeQuery = true)
    List<VentaEncabezado> findAllByIdEmpresaMesAnio(Long idEmpresa, int mes , int anio);


}
