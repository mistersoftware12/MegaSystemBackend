package com.Biblioteca.Repository.Caja;

import com.Biblioteca.Models.Caja.Caja;
import com.Biblioteca.Models.Producto.Baja.BajaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {


    @Query(value = "SELECT * FROM caja WHERE usuario_id =:idUsuario AND fecha_registro =:fechaActual", nativeQuery = true)
    Optional<Caja> findAllByIdUsuarioFecha(Long idUsuario, Date fechaActual);

    @Query(value = "SELECT * FROM caja c INNER JOIN usuario u on c.usuario_id = u.id INNER JOIN persona p on u.persona_id = p.id WHERE c.estado_cobro = false AND c.estado_cierre = true AND c.fecha_cobro is null AND p.empresa_id =:idEmpresa", nativeQuery = true)
    List<Caja> findAllByidEmpresa(Long idEmpresa);

    @Query(value = "SELECT * FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =:idEmpresa AND fecha_registro>=:fechaInicio AND fecha_registro<=:fechaFin AND estado_cobro=:estado", nativeQuery = true)
    List<Caja> findAllReporte1(Long idEmpresa, Date fechaInicio , Date fechaFin , Boolean estado );


    @Query(value = "SELECT * FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =:idEmpresa AND c.usuario_id =:idUsuario  AND fecha_registro>=:fechaInicio AND fecha_registro<=:fechaFin AND estado_cobro=:estado", nativeQuery = true)
    List<Caja> findAllReporteUsuario1(Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin, Boolean estado );

}
