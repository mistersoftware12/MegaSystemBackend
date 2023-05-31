package com.Biblioteca.Repository.Venta;


import com.Biblioteca.Models.Venta.VentaEncabezado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaEncabezadoRepository extends JpaRepository<VentaEncabezado, Long> {

    /*
    @Query(value = "SELECT * FROM categoria WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Categoria> findAllByIdEmpresa(Long idEmpresa);*/

}
