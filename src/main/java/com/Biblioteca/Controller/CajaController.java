package com.Biblioteca.Controller;

import com.Biblioteca.DTO.Caja.CajaRequest;
import com.Biblioteca.DTO.Caja.CajaRequest2;
import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Service.Caja.CajaService;
import com.Biblioteca.Service.Categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/caja")
public class CajaController {

@Autowired
CajaService cajaService;


    @GetMapping("/contarApertura/{cedulaUsuario}/{fechaActual}")
    public ResponseEntity<IdResponse> contarApertura(@PathVariable String cedulaUsuario ,@PathVariable Date fechaActual){
        IdResponse data = cajaService.consultarAperturaCaja(cedulaUsuario,fechaActual);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/registrarAperturaCaja")
    public ResponseEntity<?> registroSucursal(@RequestBody CajaRequest request){
        return new ResponseEntity<>(cajaService.registrarApertura(request), HttpStatus.OK);
    }

    @PostMapping("/registrarCierreCaja")
    public ResponseEntity<?> registrarCierreCaja(@RequestBody CajaRequest2 request){
        return new ResponseEntity<>(cajaService.cerrarCaja(request), HttpStatus.OK);
    }


}
