package com.Biblioteca.Models.Credito;


import com.Biblioteca.Models.Venta.VentaEncabezado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;


@Table(name = "credito_cliente")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditoCliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Boolean estado;

    private float valor_pendiente;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "venta_encabezado_id",referencedColumnName = "id")
    private VentaEncabezado ventaEncabezado;


}
