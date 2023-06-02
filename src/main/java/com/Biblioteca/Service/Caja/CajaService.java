package com.Biblioteca.Service.Caja;
import com.Biblioteca.DTO.Caja.CajaRequest;
import com.Biblioteca.DTO.Caja.CajaRequest2;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Caja.Caja;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Repository.Caja.CajaRepository;
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
import java.util.Optional;

@Slf4j
@Service
public class CajaService {

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

    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countApertura(Long idUsuario, Date fechaRegistro) {
        Query nativeQuery = entityManager.createNativeQuery("select count(c.id) from caja c inner join usuario u on c.usuario_id = u.id where c.usuario_id =? and c.fecha_registro =?");
        nativeQuery.setParameter(1, idUsuario);
        nativeQuery.setParameter(2, fechaRegistro);
        return (BigInteger) nativeQuery.getSingleResult();
    }


}
