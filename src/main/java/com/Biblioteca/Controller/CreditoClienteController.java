package com.Biblioteca.Controller;


import com.Biblioteca.DTO.Credito.ContenidoCreditoClienteRequest;
import com.Biblioteca.DTO.Credito.CreditoClienteResponse;
import com.Biblioteca.DTO.Extra.TipoResponse;
import com.Biblioteca.DTO.Venta.VentaEncabezadoRequest;
import com.Biblioteca.Service.Credito.CreditoClienteService;
import com.Biblioteca.Service.Tipo.TipoPagoVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/creditocliente")
public class CreditoClienteController {

@Autowired
    CreditoClienteService creditoClienteService;


    @GetMapping("/allCreditoCliente/{idEmpresa}/{estado}")
    public ResponseEntity<List<CreditoClienteResponse>> allCreditoCliente(@PathVariable long idEmpresa , @PathVariable boolean estado){
        List<CreditoClienteResponse> allSucursal = creditoClienteService.listAllCategoria(idEmpresa,estado);
        return new ResponseEntity<>(allSucursal, HttpStatus.OK);
    }


    @PostMapping("/registrarPagoCredito")
    public ResponseEntity<?> registrarPagoCredito(@RequestBody ContenidoCreditoClienteRequest request){
        return new ResponseEntity<>(creditoClienteService.registrarContenidoCredito(request), HttpStatus.OK);
    }


}
