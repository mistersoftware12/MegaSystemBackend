package com.Biblioteca.Repository.Caja;

import com.Biblioteca.Models.Caja.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {


    @Query(value = "SELECT * FROM caja WHERE usuario_id =:idUsuario AND fecha_registro =:fechaActual", nativeQuery = true)
    Optional<Caja> findAllByIdUsuarioFecha(Long idUsuario, Date fechaActual);

    @Query(value = "SELECT * FROM caja c INNER JOIN usuario u on c.usuario_id = u.id INNER JOIN persona p on u.persona_id = p.id WHERE c.estado_cobro = false AND c.fecha_cobro is null AND p.empresa_id =:idEmpresa", nativeQuery = true)
    List<Caja> findAllByidEmpresa(Long idEmpresa);

}
