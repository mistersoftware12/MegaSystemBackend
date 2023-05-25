package com.Biblioteca.Models.Producto.Baja;

import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "producto_baja")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BajaProducto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observacion;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ingreso_baja_producto_id",referencedColumnName = "id")
    private IngresoBajaProducto ingresoBajaProducto;


}
