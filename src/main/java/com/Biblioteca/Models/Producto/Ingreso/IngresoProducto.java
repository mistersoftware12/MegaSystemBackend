package com.Biblioteca.Models.Producto.Ingreso;

import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "producto_ingreso")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngresoProducto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ingreso_baja_producto_id",referencedColumnName = "id")
    private IngresoBajaProducto ingresoBajaProducto;


}
