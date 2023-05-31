package com.Biblioteca.Controller;


import com.Biblioteca.DTO.Extra.TipoResponse;
import com.Biblioteca.Service.Tipo.TipoPagoVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/tipo")
public class TipoController {

@Autowired
    TipoPagoVentaService tipoPagoVentaService;


    @GetMapping("/allTipoPagoVenta")
    public ResponseEntity<List<TipoResponse>> allTipoPago(){
        List<TipoResponse> allSucursal = tipoPagoVentaService.listAllTipoPago();
        return new ResponseEntity<>(allSucursal, HttpStatus.OK);
    }


}
