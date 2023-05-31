package com.Biblioteca.Models.Tipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "tipo_contenido")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoContenido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

}
