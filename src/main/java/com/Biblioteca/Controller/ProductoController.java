package com.Biblioteca.Controller;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Producto.ProductoRequest;
import com.Biblioteca.DTO.Producto.ProductoResponse;
import com.Biblioteca.DTO.Producto.ProductoResponse1;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Service.Categoria.CategoriaService;
import com.Biblioteca.Service.Producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/producto")
public class ProductoController {

@Autowired
    CategoriaService categoriaService;

@Autowired
    ProductoService productoService;


    @PostMapping("/registrarProducto")
    public ResponseEntity<?> registrarProducto(@RequestBody ProductoRequest request){
        return new ResponseEntity<>(productoService.regitrarProducto(request), HttpStatus.OK);
    }

    @GetMapping("/allProductos/{idEmpresa}")
    public ResponseEntity<List<ProductoResponse1>> allProductos(@PathVariable Long idEmpresa){
        List<ProductoResponse1> allSucursal =  productoService.listAllProductos(idEmpresa);
        return new ResponseEntity<>(allSucursal, HttpStatus.OK);
    }


    @PutMapping("/updateProducto")
    public ResponseEntity<?> updateProducto(@RequestBody ProductoRequest productoRequest){
        productoService.actualizarproducto(productoRequest);
        return new ResponseEntity(new Mensaje("Producto Actualizado"), HttpStatus.OK);
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<ProductoResponse> listProductoById(@PathVariable Long id){
        ProductoResponse user = productoService.productoById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
