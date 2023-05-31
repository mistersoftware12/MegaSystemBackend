package com.Biblioteca.Models.Venta;

import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Tipo.TipoPagoVenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "venta_encabezado")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaEncabezado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    private String observacion;

    private String secuencia;

    private float subtotal;

    private  float iva;

    private  float descuento;

    private float total;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "cliente_id",referencedColumnName = "id")
    private Cliente cliente;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "empresa_id",referencedColumnName = "id")
    private Empresa empresa;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "usuario_id",referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tipo_pago_id",referencedColumnName = "id")
    private TipoPagoVenta tipoPagoVenta;



}
