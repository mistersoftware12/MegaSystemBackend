package com.Biblioteca.Service.Tipo;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Extra.TipoResponse;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Tipo.TipoPagoVenta;
import com.Biblioteca.Repository.Tipo.TipoPagoVentaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TipoPagoVentaService {

    @Autowired
    TipoPagoVentaRepository tipoPagoVentaRepository;


    public List<TipoResponse> listAllTipoPago() {
        List<TipoPagoVenta> tipoPagoVentas = tipoPagoVentaRepository.findAll();
        return tipoPagoVentas.stream().map(dataRequest->{
            TipoResponse response = new TipoResponse();
            response.setId(dataRequest.getId());
            response.setDescripcion(dataRequest.getDescripcion());
            return response;
        }).collect(Collectors.toList());
    }
}
