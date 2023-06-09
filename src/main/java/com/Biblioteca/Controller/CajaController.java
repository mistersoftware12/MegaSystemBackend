package com.Biblioteca.Controller;

import com.Biblioteca.DTO.Caja.*;
import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Reporte.Reporte1Request;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Service.Caja.CajaService;
import com.Biblioteca.Service.Categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/registrarCobroCaja")
    public ResponseEntity<?> registrarCobroCaja(@RequestBody CajaRequest request){
        return new ResponseEntity<>(cajaService.registrarCobroCaja(request), HttpStatus.OK);
    }

    @GetMapping("/allCajaNoCobrado/{idEmpresa}")
    public ResponseEntity<List<CajaResponse>> allCajaNoCobrado(@PathVariable Long idEmpresa){
        List<CajaResponse> alldata = cajaService.listAllCajaPorCobrar(idEmpresa);
        return new ResponseEntity<>(alldata, HttpStatus.OK);
    }

    @PostMapping("/consultaResumen1")
    public ResponseEntity<?> registrconsultaResumen1arCobroCaja(@RequestBody Reporte1Request request){
        return new ResponseEntity<>(cajaService.resumen(request), HttpStatus.OK);
    }

    @PostMapping("/reporteVenta1")
    public ResponseEntity<?> reporteVenta1(@RequestBody Reporte1Request request){
        return new ResponseEntity<>(cajaService.reporteVentas(request), HttpStatus.OK);
    }

}
