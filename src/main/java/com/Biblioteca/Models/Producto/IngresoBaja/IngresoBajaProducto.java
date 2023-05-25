package com.Biblioteca.Models.Producto.IngresoBaja;

import com.Biblioteca.Models.Producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "producto_ingreso_baja")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngresoBajaProducto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float cantidad;

    private  float precioCompra;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "producto_id",referencedColumnName = "id")
    private Producto producto;


}
