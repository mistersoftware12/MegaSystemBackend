package com.Biblioteca.Repository.Venta;


import com.Biblioteca.Models.Venta.VentaContenido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaContenidoRepository extends JpaRepository<VentaContenido, Long> {

    /*
    @Query(value = "SELECT * FROM categoria WHERE  empresa_id = :idEmpresa", nativeQuery = true)
    List<Categoria> findAllByIdEmpresa(Long idEmpresa);*/

}
