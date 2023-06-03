package com.Biblioteca.DTO.Producto.IngresoBajaProducto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class IngresoBajaProductoRequest implements Serializable {

    private Long id;
    private float cantidad;
    private float precioCompra;
    private Date fechaRegistro;
    private Long idProducto;
    private String observacion;
    private String cedulaUsuario;

}
