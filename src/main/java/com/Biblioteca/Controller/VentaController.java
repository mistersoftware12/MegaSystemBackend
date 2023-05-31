package com.Biblioteca.Controller;


import com.Biblioteca.DTO.Venta.VentaContenidoRequest;
import com.Biblioteca.DTO.Venta.VentaEncabezadoRequest;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Service.Venta.VentaEncabezadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/venta")
public class VentaController {

@Autowired
    VentaEncabezadoService ventaEncabezadoService;



    @PostMapping("/registrarVenta")
    public ResponseEntity<?> registrarVenta(@RequestBody VentaEncabezadoRequest request){
        return new ResponseEntity<>(ventaEncabezadoService.regitrarVenta(request), HttpStatus.OK);
    }

    @PostMapping("/registrarContenidoVenta/{idVentaEncabezado}")
    public ResponseEntity<?> registrarContenidoVenta(@RequestBody VentaContenidoRequest request, @PathVariable Long idVentaEncabezado){
        return new ResponseEntity<>(ventaEncabezadoService.registrarContenido(request, idVentaEncabezado), HttpStatus.OK);
    }

    /*
    @GetMapping("/allCategorias/{idEmpresa}")
    public ResponseEntity<List<CategoriaRequest>> allCategorias(@PathVariable Long idEmpresa){
        List<CategoriaRequest> allSucursal = categoriaService.listAllCategoria(idEmpresa);
        return new ResponseEntity<>(allSucursal, HttpStatus.OK);
    }


    @PutMapping("/updateCategoria")
    public ResponseEntity<?> updateSucursal(@RequestBody CategoriaRequest categoriaRequest){
        categoriaService.actualizarcategoria(categoriaRequest);
        return new ResponseEntity(new Mensaje("Categoria Actualizado"), HttpStatus.OK);
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaRequest> listProveedorById(@PathVariable Long id){
        CategoriaRequest user = categoriaService.categoriaById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
*/

}
