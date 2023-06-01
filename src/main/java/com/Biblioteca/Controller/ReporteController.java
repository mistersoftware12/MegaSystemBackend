package com.Biblioteca.Controller;



import com.Biblioteca.DTO.Reporte.Reporte1Response;
import com.Biblioteca.Service.Credito.CreditoClienteService;
import com.Biblioteca.Service.Venta.VentaEncabezadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/reporte")
public class ReporteController {

@Autowired
    VentaEncabezadoService ventaEncabezadoService;


    @GetMapping("/cierredecaja/{cedulaUsuario}/{fecha}")
    public ResponseEntity<Reporte1Response> cierredecaja(@PathVariable String cedulaUsuario , @PathVariable Date fecha){
        Reporte1Response data = ventaEncabezadoService.reporteCierreCaja(cedulaUsuario,fecha);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
