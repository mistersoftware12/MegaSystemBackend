package com.Biblioteca.Models.Produccion;

import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "produccion_contenido")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoProduccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  float cantidad;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "produccion_id",referencedColumnName = "id")
    private Produccion produccion;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "producto_id",referencedColumnName = "id")
    private Producto producto;

}
