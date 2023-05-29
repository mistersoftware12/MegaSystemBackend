package com.Biblioteca.Repository.Produccion;

import com.Biblioteca.Models.Produccion.ContenidoProduccion;
import com.Biblioteca.Models.Produccion.Produccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContenidoProduccionRepository extends JpaRepository<ContenidoProduccion, Long> {


    @Query(value = "SELECT * FROM public.produccion_contenido WHERE produccion_id = :idProduccion", nativeQuery = true)
    List<ContenidoProduccion> findAllByIdEmpresa(Long idProduccion);
}
