package com.Biblioteca.Service.Caja;
import com.Biblioteca.DTO.Caja.*;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Reporte.Reporte1Request;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Caja.Caja;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Produccion.Produccion;
import com.Biblioteca.Models.Producto.Baja.BajaProducto;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import com.Biblioteca.Models.Producto.Producto;
import com.Biblioteca.Models.Venta.VentaContenido;
import com.Biblioteca.Repository.Caja.CajaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.UsuarioRepository;
import com.Biblioteca.Repository.Produccion.ProduccionRepository;
import com.Biblioteca.Repository.Producto.Baja.BajaProductoRepository;
import com.Biblioteca.Repository.Producto.Ingreso.IngresoProductoRepository;
import com.Biblioteca.Repository.Producto.ProductoRepository;
import com.Biblioteca.Repository.Producto.Venta.VentaContenidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CajaService {
    @Autowired
    private BajaProductoRepository bajaProductoRepository;
    @Autowired
    private IngresoProductoRepository ingresoProductoRepository;
    @Autowired
    private ProduccionRepository produccionRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private VentaContenidoRepository ventaContenidoRepository;
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

    public CajaResponse2 reporteVentas(Reporte1Request request){
        CajaResponse2 response = new CajaResponse2();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMMMM/yyyy ", Locale.forLanguageTag("es-EC"));
        response.setFechaInicioS(formatter.format(request.getFechaInicio()));
        response.setFechaFinS(formatter.format(request.getFechaFin()));
        response.setListaContenido(reporteVentas2(request));
        return response;
    }

    public List<CajaContenidoResponse> reporteVentas2(Reporte1Request request){

       //numero 1 informe de ventas
       if(request.getNumero()==1){
           List<VentaContenido> cajas = null;
           if(request.getIdUsuario() == 0) {

               cajas = ventaContenidoRepository.findAllReporte1(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin());

           }else{
               cajas = ventaContenidoRepository.findAllReporteUsuario1(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin());
           }

           return cajas.stream().map(dataRequest->{
               CajaContenidoResponse response = new CajaContenidoResponse();
               response.setSecuencia(dataRequest.getVentaEncabezado().getSecuencia());
               response.setFecha(dataRequest.getVentaEncabezado().getFechaEmision());
               response.setNombreUsuario(dataRequest.getVentaEncabezado().getUsuario().getPersona().getNombres()+" "+dataRequest.getVentaEncabezado().getUsuario().getPersona().getApellidos());

               if(dataRequest.getTipoContenido().getId() == 1){
                   Optional<Producto> optionalProducto1 = productoRepository.findById(dataRequest.getIdProducto());
                   if(optionalProducto1.isPresent()){
                       response.setNombreProducto(optionalProducto1.get().getNombre());
                   }else{
                       response.setNombreProducto("");
                   }

               }
               if(dataRequest.getTipoContenido().getId() == 2){
                   Optional<Produccion> optionalProducto5 = produccionRepository.findById(dataRequest.getIdProducto());
                   if(optionalProducto5.isPresent()){
                       response.setNombreProducto(optionalProducto5.get().getNombre());
                   }else{
                       response.setNombreProducto("");
                   }

               }
               response.setCantidad(dataRequest.getCantidad());
               response.setPrecioUnitario(dataRequest.getPrecioUnitario());
               response.setPrecioIva(dataRequest.getPrecioIva());
               response.setTotal(dataRequest.getPrecioTotal());
               response.setGanancia(dataRequest.getGanancia());

               return response;
           }).collect(Collectors.toList());
       }else{

           if(request.getNumero() == 2){
               //numero 2 informe entradas
               List<IngresoProducto> cajas = null;
               if(request.getIdUsuario() == 0) {

                   cajas = ingresoProductoRepository.findAllReporte1(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin());

               }else{
                   cajas = ingresoProductoRepository.findAllReporteUsuario1(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin());
               }

               return cajas.stream().map(dataRequest->{
                   CajaContenidoResponse response = new CajaContenidoResponse();
                   response.setFecha(dataRequest.getIngresoBajaProducto().getFechaRegistro());
                   response.setNombreUsuario(dataRequest.getIngresoBajaProducto().getUsuario().getPersona().getNombres()+" "+dataRequest.getIngresoBajaProducto().getUsuario().getPersona().getApellidos());
                   response.setCantidad(dataRequest.getIngresoBajaProducto().getCantidad());
                   response.setPrecioUnitario(dataRequest.getIngresoBajaProducto().getPrecioCompra());
                   response.setTotal(dataRequest.getIngresoBajaProducto().getCantidad() * dataRequest.getIngresoBajaProducto().getPrecioCompra());
                   response.setNombreProducto(dataRequest.getIngresoBajaProducto().getProducto().getNombre());
                   return response;
               }).collect(Collectors.toList());
           }else{
               if(request.getNumero() == 3){
                   //numero 3 informe baja
                   List<BajaProducto> cajas = null;
                   if(request.getIdUsuario() == 0) {

                       cajas = bajaProductoRepository.findAllReporte1(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin());

                   }else{
                       cajas = bajaProductoRepository.findAllReporteUsuario1(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin());
                   }

                   return cajas.stream().map(dataRequest->{
                       CajaContenidoResponse response = new CajaContenidoResponse();
                       response.setFecha(dataRequest.getIngresoBajaProducto().getFechaRegistro());
                       response.setNombreUsuario(dataRequest.getIngresoBajaProducto().getUsuario().getPersona().getNombres()+" "+dataRequest.getIngresoBajaProducto().getUsuario().getPersona().getApellidos());
                       response.setCantidad(dataRequest.getIngresoBajaProducto().getCantidad());
                       response.setPrecioUnitario(dataRequest.getIngresoBajaProducto().getPrecioCompra());
                       response.setTotal(dataRequest.getIngresoBajaProducto().getCantidad() * dataRequest.getIngresoBajaProducto().getPrecioCompra());
                       response.setNombreProducto(dataRequest.getIngresoBajaProducto().getProducto().getNombre());
                       response.setSecuencia(dataRequest.getObservacion());
                       return response;
                   }).collect(Collectors.toList());
               }else{
                   if(request.getNumero() == 4){
                       //numero 4 cobrado
                       List<Caja> cajas = null;
                       if(request.getIdUsuario() == 0) {
                           cajas = cajaRepository.findAllReporte1(request.getIdEmpresa(), request.getFechaInicio(), request.getFechaFin(),true);
                       }else{
                           cajas = cajaRepository.findAllReporteUsuario1(request.getIdEmpresa(), request.getIdUsuario(), request.getFechaInicio(), request.getFechaFin(),true);
                       }

                       return cajas.stream().map(dataRequest->{
                           CajaContenidoResponse response = new CajaContenidoResponse();
                           response.setFecha(dataRequest.getFechaRegistro());
                           response.setNombreUsuario(dataRequest.getUsuario().getPersona().getNombres()+" "+dataRequest.getUsuario().getPersona().getApellidos());
                           response.setTotal(dataRequest.getTotal_efectivo());
                           response.setSaldoApertura(dataRequest.getSaldo_apertura());
                           response.setTotalVenta(dataRequest.getTotal_venta());
                           response.setFechaCobro(dataRequest.getFechaCobro());
                           return response;
                       }).collect(Collectors.toList());
                   }else{
                       return null;
                   }
               }
           }

       }



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


    public CajaResponse3 resumenGeneral(Long idEmpresa)   {
        CajaResponse3 caja = new CajaResponse3();
        caja.setProductosTotal(consultaProductoTotal(idEmpresa));
        caja.setStockTotal(consultaStockTotal(idEmpresa));
        caja.setCompraEstimada(consultaCompraEstimada(idEmpresa));
        caja.setVentaEstimada(consultaVentaEstimada(idEmpresa));
        caja.setGananciaEstimada(consultaGananciaEstimada(idEmpresa));
            return caja;
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

//Consulta general
public BigInteger consultaProductoTotal (Long idEmpresa) {

        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(ID) FROM producto WHERE empresa_id =?");
        nativeQuery.setParameter(1, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();

}

    public float consultaStockTotal (Long idEmpresa) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(stock)  FROM producto WHERE empresa_id =?");
            nativeQuery.setParameter(1, idEmpresa);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consultaCompraEstimada (Long idEmpresa) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(stock * precio_compra)  FROM producto WHERE empresa_id =?");
            nativeQuery.setParameter(1, idEmpresa);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consultaVentaEstimada (Long idEmpresa) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(stock * precio_venta)  FROM producto WHERE empresa_id =?");
            nativeQuery.setParameter(1, idEmpresa);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

    public float consultaGananciaEstimada (Long idEmpresa) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM((stock * precio_venta)-(stock * precio_compra))  FROM producto WHERE empresa_id =?");
            nativeQuery.setParameter(1, idEmpresa);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return  0;
        }
    }

/*
   private float gananciaEstimada;
 */
}
