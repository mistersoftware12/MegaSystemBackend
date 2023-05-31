package com.Biblioteca.Models.Venta;

import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Tipo.TipoContenido;
import com.Biblioteca.Models.Tipo.TipoPagoVenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "venta_contenido")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaContenido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  float cantidad;
    private  float precioUnitario;
    private  float precioIva;
    private  float precioTotal;
    private  float ganancia;
    private long idProducto;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tipo_contenido_id",referencedColumnName = "id")
    private TipoContenido tipoContenido;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "venta_encabezado_id",referencedColumnName = "id")
    private VentaEncabezado ventaEncabezado;

}
