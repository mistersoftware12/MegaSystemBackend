package com.Biblioteca.Repository.Credito;

import com.Biblioteca.Models.Credito.ContenidoCreditoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContenidoCreditoClienteRepository extends JpaRepository<ContenidoCreditoCliente, Long> {

    /*
    @Query(value = "SELECT * FROM credito_cliente c JOIN venta_encabezado v on c.venta_encabezado_id  = v.id WHERE c.estado =:estado AND v.empresa_id =:idEmpresa ORDER BY v.secuencia DESC", nativeQuery = true)
    List<CreditoCliente> findAllByIdEmpresa(Long idEmpresa , boolean estado);
*/

}
