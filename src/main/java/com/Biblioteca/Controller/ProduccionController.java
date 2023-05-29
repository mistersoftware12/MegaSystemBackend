package com.Biblioteca.Controller;

import com.Biblioteca.DTO.Produccion.ContenidoProduccionRequest;
import com.Biblioteca.DTO.Produccion.ProduccionRequest;
import com.Biblioteca.DTO.Produccion.ProduccionResponse;
import com.Biblioteca.DTO.Producto.ProductoRequest;
import com.Biblioteca.DTO.Producto.ProductoResponse;
import com.Biblioteca.DTO.Producto.ProductoResponse1;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Service.Categoria.CategoriaService;
import com.Biblioteca.Service.Produccion.ProduccionService;
import com.Biblioteca.Service.Producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/produccion")
public class ProduccionController {

@Autowired
    CategoriaService categoriaService;

@Autowired
    ProductoService productoService;

@Autowired
    ProduccionService produccionService;


    @PostMapping("/registrarProduccion")
    public ResponseEntity<?> registrarProduccion(@RequestBody ProduccionRequest request){
        return new ResponseEntity<>(produccionService.regitrarProduccion(request), HttpStatus.OK);
    }

    @GetMapping("/allProduccion/{idEmpresa}")
    public ResponseEntity<List<ProduccionResponse>> allProductos(@PathVariable Long idEmpresa){
        List<ProduccionResponse> allSucursal =  produccionService.listAllProduccion(idEmpresa);
        return new ResponseEntity<>(allSucursal, HttpStatus.OK);
    }

    @GetMapping("/produccion/{id}")
    public ResponseEntity<ProduccionResponse> listProduccionById(@PathVariable Long id){
        ProduccionResponse user = produccionService.produccionById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/produccionBarra/{idEmpresa}/{codigoBarra}")
    public ResponseEntity<ProduccionResponse> listProduccionBarra(@PathVariable Long idEmpresa, @PathVariable String codigoBarra){
        ProduccionResponse user = produccionService.produccionCodigoBarra(idEmpresa,codigoBarra);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/updateProduccion")
    public ResponseEntity<?> updateProduccion(@RequestBody ProduccionRequest produccionRequest){
        produccionService.actualizarproduccion(produccionRequest);
        return new ResponseEntity(new Mensaje("Producto Actualizado"), HttpStatus.OK);
    }

////////////////////////////Produccion


    @PostMapping("/registrarContenidoProduccion")
    public ResponseEntity<?> registrarContenidoProduccion(@RequestBody ContenidoProduccionRequest request){
        return new ResponseEntity<>(produccionService.regitrarContenidoProduccion(request), HttpStatus.OK);
    }

    @PutMapping("/updateContenidoProduccion")
    public ResponseEntity<?> updateContenidoProduccion(@RequestBody ContenidoProduccionRequest contenidoProduccionRequest){
        produccionService.actualizarContenidoproduccion(contenidoProduccionRequest);
        return new ResponseEntity(new Mensaje("Producto Actualizado"), HttpStatus.OK);
    }




}
