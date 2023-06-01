package com.Biblioteca.Models.Credito;


import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Venta.VentaEncabezado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "credito_cliente_contenido")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoCreditoCliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    private float valor;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "credito_cliente_id",referencedColumnName = "id")
    private CreditoCliente creditoCliente;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "usuario_id",referencedColumnName = "id")
    private Usuario Usuario;

}
