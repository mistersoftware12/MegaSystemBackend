package com.Biblioteca.Service.Caja;
import com.Biblioteca.DTO.Caja.*;
import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Reporte.Reporte1Request;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Caja.Caja;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Repository.Caja.CajaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CajaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
   UsuarioRepository usuarioRepository;

   @Autowired
    CajaRepository cajaRepository;


   public  IdResponse registrarApertura(CajaRequest cajaRequest){

       Optional<Usuario> optionalUsuario = usuarioRepository.findByCedula(cajaRequest.getCedulaUsuario());
       if(optionalUsuario.isPresent()){

           if(countApertura(optionalUsuario.get().getId(), cajaRequest.getFechaActual()) ==  BigInteger.valueOf(0)){
               Caja caja = new Caja();
               caja.setUsuario(optionalUsuario.get());
               caja.setSaldo_apertura(cajaRequest.getSaldoApertura());
               caja.setFechaRegistro(cajaRequest.getFechaActual());
               caja.setEstadoCobro(false);
               caja.setEstadoCierre(false);

               try {
                   cajaRepository.save(caja);
                   IdResponse response = new IdResponse();
                   response.setId(caja.getId());
                   return response;
               }catch (Exception e){
                   throw new BadRequestException("No se registró la sucursal" +e);
               }


           }else{
               throw new BadRequestException("Ya existe una apertura para el usuario "+optionalUsuario.get().getPersona().getNombres());
           }
       }else{
           throw new BadRequestException("No existe un usuario con cédula "+cajaRequest.getCedulaUsuario());
       }

   }

    public IdResponse consultarAperturaCaja(String cedulaUsuario, Date fechaRegistro){
        Optional<Usuario> data = usuarioRepository.findByCedula(cedulaUsuario);
        if(data.isPresent()){
            IdResponse idResponse = new IdResponse();
            Optional<Caja> optionalCaja = cajaRepository.findAllByIdUsuarioFecha(data.get().getId(),fechaRegistro);
            if(optionalCaja.isPresent()){
                idResponse.setId(optionalCaja.get().getId());
                idResponse.setEstado(optionalCaja.get().isEstadoCierre());
            }else{
                idResponse.setId(0L);
            }

            return  idResponse;
        }else{
            throw new BadRequestException("No existe un usuario con cédula "+cedulaUsuario);
        }

    }


    public boolean cerrarCaja(CajaRequest2 cajaRequest2){
       Optional<Caja> optionalCaja = cajaRepository.findById(cajaRequest2.getId());
       if(optionalCaja.isPresent()){
           optionalCaja.get().setEstadoCierre(true);
           optionalCaja.get().setTotal_efectivo(cajaRequest2.getTotalEfectivo());
           optionalCaja.get().setTotal_venta(cajaRequest2.getTotalVenta());
           try {
               cajaRepository.save( optionalCaja.get());
               return true;
           }catch (Exception e){
               throw new BadRequestException("No se registró la sucursal" +e);
           }

       }else{
           throw new BadRequestException("No existe un cierre de caja con id "+cajaRequest2.getId());
       }
    }

    public boolean registrarCobroCaja(CajaRequest cajaRequest){

        Optional<Caja> optionalCaja = cajaRepository.findById(cajaRequest.getId());
        if(optionalCaja.isPresent()){
            optionalCaja.get().setEstadoCobro(true);
            optionalCaja.get().setFechaCobro(cajaRequest.getFechaActual());
            try {
                cajaRepository.save( optionalCaja.get());
                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró la sucursal" +e);
            }

        }else{
            throw new BadRequestException("No existe un cierre de caja con id "+cajaRequest.getId());
        }
    }

    public List<CajaResponse> listAllCajaPorCobrar(Long idEmpresa) {
        List<Caja> cajas = cajaRepository.findAllByidEmpresa(idEmpresa);
        return cajas.stream().map(dataRequest->{
            CajaResponse response = new CajaResponse();
            response.setId(dataRequest.getId());
            response.setNombreUsuario(dataRequest.getUsuario().getPersona().getNombres()+" "+dataRequest.getUsuario().getPersona().getApellidos());
            response.setFechaCaja(dataRequest.getFechaRegistro());
            response.setSaldoApertura(dataRequest.getSaldo_apertura());
            response.setSaltoEfectivo(dataRequest.getTotal_efectivo());
            return response;
        }).collect(Collectors.toList());
    }

    public CajaResponse1 resumen(Reporte1Request request)   {

       if(request.getIdUsuario() == 0){
           CajaResponse1 caja = new CajaResponse1();
           caja.setSubotal(consulta1Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setDescuento(0);
           caja.setIva(consulta3Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setTotal(consulta4Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setGanancia(consulta5Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setEntrada(consulta6Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setBaja(consulta7Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));
           caja.setCobrado(consulta8G(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin() , true));
           caja.setPorCobrar(consulta8G(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin() , false));
           caja.setApertura(consulta9Ge(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin()));

           return caja;
       }else {


           Optional<Usuario> data = usuarioRepository.findById(request.getIdUsuario());
           if (data.isPresent()) {
               Optional<Empresa> data2 = empresaRepository.findById(request.getIdEmpresa());
               if (data2.isPresent()) {
                   CajaResponse1 caja = new CajaResponse1();
                   caja.setSubotal(consulta1(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setDescuento(0);
                   caja.setIva(consulta3(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setTotal(consulta4(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setGanancia(consulta5(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setEntrada(consulta6(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setBaja(consulta7(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));
                   caja.setCobrado(consulta8(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin() , true));
                   caja.setPorCobrar(consulta8(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin() , false));
                  caja.setApertura(consulta9(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin()));

                   return caja;
               } else {
                   throw new BadRequestException("No existe una empresa com id " + request.getIdEmpresa());
               }
           } else {
               throw new BadRequestException("No existe un usuario con id " + request.getIdUsuario());
           }
       }
    }


    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countApertura(Long idUsuario, Date fechaRegistro) {
        Query nativeQuery = entityManager.createNativeQuery("select count(c.id) from caja c inner join usuario u on c.usuario_id = u.id where c.usuario_id =? and c.fecha_registro =?");
        nativeQuery.setParameter(1, idUsuario);
        nativeQuery.setParameter(2, fechaRegistro);
        return (BigInteger) nativeQuery.getSingleResult();
    }



    public float consulta1Ge (Long idEmpresa,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * precio_unitario) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consulta3Ge (Long idEmpresa, Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * precio_iva) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }

    }

    public float consulta4Ge (Long idEmpresa,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(precio_total) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =?  AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }


    }

    public float consulta5Ge (Long idEmpresa, Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * ganancia) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    //INGRESO
    public float consulta6Ge (Long idEmpresa, Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(p.precio_compra * p.cantidad) FROM producto_ingreso i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =?  AND fecha_registro>=? AND fecha_registro<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    //BAJA

    public float consulta7Ge (Long idEmpresa ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(p.precio_compra * p.cantidad) FROM producto_baja i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =?  AND fecha_registro>=? AND fecha_registro<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    //cobros
    public float consulta8G (Long idEmpresa, Date fechaInicio , Date fechaFin , Boolean estado ) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(c.total_efectivo) FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =? AND fecha_registro>=? AND fecha_registro<=? AND estado_cobro=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, fechaInicio);
            nativeQuery.setParameter(3, fechaFin);
            nativeQuery.setParameter(4, estado);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consulta9Ge (Long idEmpresa,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(c.saldo_apertura) FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =?  AND fecha_registro>=? AND fecha_registro<=? AND estado_cierre=false");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consulta1 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * precio_unitario) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND e.usuario_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consulta3 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * precio_iva) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND e.usuario_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }

    }

    public float consulta4 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(precio_total) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND e.usuario_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }


    }

    public float consulta5 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(cantidad * ganancia) FROM venta_contenido c INNER JOIN venta_encabezado e ON c.venta_encabezado_id = e.id WHERE e.empresa_id =? AND e.usuario_id =? AND fecha_emision>=? AND fecha_emision<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    //INGRESO
    public float consulta6 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(p.precio_compra * p.cantidad) FROM producto_ingreso i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =? AND P.usuario_id =?  AND fecha_registro>=? AND fecha_registro<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    //BAJA

    public float consulta7 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(p.precio_compra * p.cantidad) FROM producto_baja i INNER JOIN producto_ingreso_baja p ON i.ingreso_baja_producto_id = p.id INNER JOIN producto pr ON p.producto_id = pr.id WHERE pr.empresa_id =? AND P.usuario_id =?  AND fecha_registro>=? AND fecha_registro<=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }


    //cobros
    public float consulta8 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin , Boolean estado ) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(c.total_efectivo) FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =? AND c.usuario_id =?  AND fecha_registro>=? AND fecha_registro<=? AND estado_cobro=?");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            nativeQuery.setParameter(5, estado);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consulta9 (Long idEmpresa, Long idUsuario ,Date fechaInicio , Date fechaFin) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(c.saldo_apertura) FROM caja c INNER JOIN usuario u ON c.usuario_id = u.id INNER JOIN persona p ON u.persona_id = p.id  WHERE p.empresa_id =? AND c.usuario_id =?  AND fecha_registro>=? AND fecha_registro<=? AND estado_cierre=false");
            nativeQuery.setParameter(1, idEmpresa);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fechaInicio);
            nativeQuery.setParameter(4, fechaFin);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }


}
