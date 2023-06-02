package com.Biblioteca.Service.Venta;

import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Produccion.ContenidoProduccionResponse;
import com.Biblioteca.DTO.Reporte.Reporte1Response;
import com.Biblioteca.DTO.Venta.VentaContenidoRequest;
import com.Biblioteca.DTO.Venta.VentaEncabezadoRequest;
import com.Biblioteca.DTO.Venta.VentaEncabezadoResponse;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Credito.CreditoCliente;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Produccion.ContenidoProduccion;
import com.Biblioteca.Models.Producto.Producto;
import com.Biblioteca.Models.Tipo.TipoContenido;
import com.Biblioteca.Models.Tipo.TipoPagoVenta;
import com.Biblioteca.Models.Venta.VentaContenido;
import com.Biblioteca.Models.Venta.VentaEncabezado;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Credito.CreditoClienteRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.ClienteRepository;
import com.Biblioteca.Repository.Persona.UsuarioRepository;
import com.Biblioteca.Repository.Produccion.ContenidoProduccionRepository;
import com.Biblioteca.Repository.Producto.ProductoRepository;
import com.Biblioteca.Repository.Tipo.TipoContenidoRepository;
import com.Biblioteca.Repository.Tipo.TipoPagoVentaRepository;
import com.Biblioteca.Repository.Venta.VentaContenidoRepository;
import com.Biblioteca.Repository.Venta.VentaEncabezadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class VentaEncabezadoService {
    @Autowired
    private CreditoClienteRepository creditoClienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ContenidoProduccionRepository contenidoProduccionRepository;
    @Autowired
    private VentaContenidoRepository ventaContenidoRepository;
    @Autowired
    private TipoContenidoRepository tipoContenidoRepository;
    @Autowired
    private VentaEncabezadoRepository ventaEncabezadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private TipoPagoVentaRepository tipoPagoVentaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;




    public IdResponse regitrarVenta (VentaEncabezadoRequest ventaEncabezadoRequest ){

        Optional<Empresa> optionalEmpresa = empresaRepository.findById(ventaEncabezadoRequest.getIdEmpresa());
        if(optionalEmpresa.isPresent()){
            Optional<TipoPagoVenta> optionalTipoPagoVenta = tipoPagoVentaRepository.findById(ventaEncabezadoRequest.getIdTipoPago());
            if(optionalTipoPagoVenta.isPresent()){
                Optional<Cliente> optionalCliente = clienteRepository.findAllByIdIdEmpresa(ventaEncabezadoRequest.getIdEmpresa(), ventaEncabezadoRequest.getIdCliente());
                if(optionalCliente.isPresent()){
                    Optional<Usuario> optionalUsuario = usuarioRepository.findByIdCedulaEmpresa(ventaEncabezadoRequest.getIdEmpresa(), ventaEncabezadoRequest.getCedulaUsuario());
                    if(optionalUsuario.isPresent()){

                        VentaEncabezado ventaEncabezado = new VentaEncabezado();
                        ventaEncabezado.setCliente(optionalCliente.get());
                        ventaEncabezado.setUsuario(optionalUsuario.get());
                        ventaEncabezado.setEmpresa(optionalEmpresa.get());
                        ventaEncabezado.setTipoPagoVenta(optionalTipoPagoVenta.get());
                        ventaEncabezado.setFechaEmision(ventaEncabezadoRequest.getFechaEmision());
                        ventaEncabezado.setObservacion(ventaEncabezadoRequest.getObservacion());
                        ventaEncabezado.setSecuencia(codigoSecuencia(ventaEncabezadoRequest.getIdEmpresa()));
                        ventaEncabezado.setSubtotal(ventaEncabezadoRequest.getSubtotal());
                        ventaEncabezado.setIva(ventaEncabezadoRequest.getIva());
                        ventaEncabezado.setTotal(ventaEncabezadoRequest.getTotal());

                        try {
                            ventaEncabezadoRepository.save(ventaEncabezado);

                            if(ventaEncabezadoRequest.getIdTipoPago() ==2){

                                CreditoCliente creditoCliente = new CreditoCliente();
                                creditoCliente.setEstado(false);
                                creditoCliente.setValor_pendiente(ventaEncabezadoRequest.getTotal());
                                creditoCliente.setVentaEncabezado(ventaEncabezado);

                             try {
                                 creditoClienteRepository.save(creditoCliente);
                             }catch (Exception e){
                                 throw new BadRequestException("No se guardo el credito" +e);
                             }
                            }

                            return new IdResponse(ventaEncabezado.getId());

                        }catch (Exception e){
                            throw new BadRequestException("No se registró el la venta" +e);
                        }
                    }else{
                        throw  new BadRequestException("No existe un usuario con cédula "+ventaEncabezadoRequest.getCedulaUsuario());
                    }
                }else{
                    throw  new BadRequestException("No existe una cliente con id "+ventaEncabezadoRequest.getIdCliente());
                }
            }else{
                throw  new BadRequestException("No existe una tipo de pago con id "+ventaEncabezadoRequest.getIdTipoPago());
            }
        }else{
            throw  new BadRequestException("No existe una empresa con id "+ventaEncabezadoRequest.getIdEmpresa());
        }

    }
    public Boolean registrarContenido(VentaContenidoRequest ventaContenidoRequest , Long idVentaEncabezado){

        Optional<TipoContenido> optionalTipoContenido =  tipoContenidoRepository.findById(ventaContenidoRequest.getTipo());
        if(optionalTipoContenido.isPresent()){
            Optional<VentaEncabezado> optionalVentaEncabezado = ventaEncabezadoRepository.findById(idVentaEncabezado);
            if(optionalVentaEncabezado.isPresent()){
                VentaContenido ventaContenido= new VentaContenido();
                ventaContenido.setTipoContenido(optionalTipoContenido.get());
                ventaContenido.setVentaEncabezado(optionalVentaEncabezado.get());
                ventaContenido.setCantidad(ventaContenidoRequest.getCantidad());
                ventaContenido.setPrecioUnitario(ventaContenidoRequest.getPrecioUnitario());
                ventaContenido.setPrecioIva(ventaContenidoRequest.getPrecioIva());
                ventaContenido.setPrecioTotal(ventaContenidoRequest.getPrecioTotal());
                ventaContenido.setGanancia(ventaContenidoRequest.getGanancia());
                ventaContenido.setIdProducto(ventaContenidoRequest.getIdProducto());

                try {
                    ventaContenidoRepository.save(ventaContenido);
                    if(ventaContenidoRequest.getTipo() == 1){
                        restarStock(ventaContenidoRequest.getIdProducto() , ventaContenidoRequest.getCantidad());
                    }

                    if(ventaContenidoRequest.getTipo() == 2){
                        listAllContenidoProduccion(ventaContenidoRequest.getIdProducto(),  ventaContenidoRequest.getCantidad());
                    }

                    return true;

                }catch (Exception e){
                    throw new BadRequestException("No se registró el contenido" +e);
                }

            }else{
                throw  new BadRequestException("No existe una venta Encabezado con id "+idVentaEncabezado);
            }

        }else{
            throw  new BadRequestException("No existe una tipo contenido con id "+ventaContenidoRequest.getTipo());
        }

    }
    public List<ContenidoProduccionResponse> listAllContenidoProduccion(Long idProduccion , float cantidad) {
        List<ContenidoProduccion> productos = contenidoProduccionRepository.findAllByIdEmpresa(idProduccion);
        return productos.stream().map(sucursalRequest->{
            ContenidoProduccionResponse response = new ContenidoProduccionResponse();

            restarStock(sucursalRequest.getProducto().getId() ,(sucursalRequest.getCantidad()*cantidad));
            return response;
        }).collect(Collectors.toList());
    }
    public List<VentaEncabezadoResponse> listAllVentas(Long idEmpresa , int mes , int anio) {
        List<VentaEncabezado> venta = ventaEncabezadoRepository.findAllByIdEmpresaMesAnio(idEmpresa,mes,anio);
        return venta.stream().map(dateRequest->{
            VentaEncabezadoResponse response = new VentaEncabezadoResponse();

            response.setId(dateRequest.getId());
            response.setSecuencia(dateRequest.getSecuencia());
            response.setFechaEmision(dateRequest.getFechaEmision());
            response.setNombreUsuario(dateRequest.getUsuario().getPersona().getNombres()+" "+dateRequest.getUsuario().getPersona().getApellidos());
            response.setCedulaCliente(dateRequest.getCliente().getPersona().getCedula());
            response.setNombreCliente(dateRequest.getCliente().getPersona().getNombres()+" "+dateRequest.getCliente().getPersona().getApellidos());
            response.setNombreTipoPago(dateRequest.getTipoPagoVenta().getDescripcion());
            response.setTotal(dateRequest.getTotal());

            return response;
        }).collect(Collectors.toList());
    }
    public boolean restarStock(Long idProducto , float cantidad){

        Optional<Producto> optionalProducto = productoRepository.findById(idProducto);
        if(optionalProducto.isPresent()){
            optionalProducto.get().setStock(countStockProducto(idProducto) - cantidad);
            try {
                productoRepository.save(optionalProducto.get());
                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró el producto" +e);
            }
        }else{
            throw new BadRequestException("No se encontro el producto con id" +idProducto);
        }
    }
    public String codigoSecuencia(Long idEmpresa){

        String valorFinal = "";
        int numero = 0 ;
        if(countordenes(idEmpresa) == BigInteger.valueOf(0)){
            valorFinal ="000000001";
        }else{

            String as= max(idEmpresa);
            Integer uno = 1;

            valorFinal = String.valueOf(Integer.valueOf(as) +uno);


            if(valorFinal.length() == 1){
                valorFinal = "00000000" +valorFinal;
            }

            if(valorFinal.length() == 2){
                valorFinal = "0000000" +valorFinal;
            }

            if(valorFinal.length() == 3){
                valorFinal = "000000" +valorFinal;
            }

            if(valorFinal.length() == 4){
                valorFinal = "00000" +valorFinal;
            }

            if(valorFinal.length() == 5){
                valorFinal = "0000" +valorFinal;
            }

            if(valorFinal.length() == 6){
                valorFinal = "000" +valorFinal;
            }

            if(valorFinal.length() == 7){
                valorFinal = "00" +valorFinal;
            }

            if(valorFinal.length() == 8){
                valorFinal = "0" +valorFinal;
            }

            if(valorFinal.length() == 9){
                valorFinal = "" +valorFinal;
            }

        }

        return valorFinal;
    }

    public Reporte1Response reporteCierreCaja(String cedulaUsuario , Date fecha){


        Reporte1Response reporte1 = new Reporte1Response();
        Optional<Usuario> optionalUsuario = usuarioRepository. findByCedula(cedulaUsuario);
        if(optionalUsuario.isPresent()){
            reporte1.setVentaEfectivo(sumReporte1(1,optionalUsuario.get().getId(),fecha));
            reporte1.setVentaCredito(sumReporte1(2,optionalUsuario.get().getId(),fecha));
            reporte1.setVentaBancaria(sumReporte1(4,optionalUsuario.get().getId(),fecha));
            reporte1.setVentaCheque(sumReporte1(3,optionalUsuario.get().getId(),fecha));
            reporte1.setVentaDebito(sumReporte1(5,optionalUsuario.get().getId(),fecha));
            reporte1.setVentaTotal(sumReporte2(optionalUsuario.get().getId(),fecha));
            reporte1.setCobroEfectivo(sumReporte1(1,optionalUsuario.get().getId(),fecha));
            reporte1.setCobroCredito(sumReporte3(optionalUsuario.get().getId(),fecha));
            reporte1.setCobroTotal(reporte1.getCobroEfectivo() +reporte1.getCobroCredito());

        }else{
            throw new BadRequestException("No existe un usuario con cédula" +cedulaUsuario);
        }

        return reporte1;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countordenes(Long idEmpresa) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(secuencia) FROM venta_encabezado WHERE empresa_id =? ");
        nativeQuery.setParameter(1, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();
    }

    public String max(Long idEmpresa) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT MAX(secuencia) FROM venta_encabezado WHERE empresa_id =? ");
        nativeQuery.setParameter(1, idEmpresa);
        return (String) nativeQuery.getSingleResult();
    }

    public float countStockProducto(Long idProducto ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT stock FROM producto WHERE id =?");
        nativeQuery.setParameter(1, idProducto);
        return (float) nativeQuery.getSingleResult();
    }

    public float sumReporte1(int tipo_pago ,Long idUsuario , Date fecha ) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(total) FROM venta_encabezado WHERE tipo_pago_id =? AND usuario_id =? AND fecha_emision =?");
            nativeQuery.setParameter(1, tipo_pago);
            nativeQuery.setParameter(2, idUsuario);
            nativeQuery.setParameter(3, fecha);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return 0;
        }

    }

    public float sumReporte2(Long idUsuario , Date fecha ) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(total) FROM venta_encabezado WHERE  usuario_id =? AND fecha_emision =?");
            nativeQuery.setParameter(1, idUsuario);
            nativeQuery.setParameter(2, fecha);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return 0;
        }

    }

    public float sumReporte3(Long idUsuario , Date fecha ) {
        try {
            Query nativeQuery = entityManager.createNativeQuery("SELECT SUM(valor) FROM credito_cliente_contenido WHERE  usuario_id =? AND fecha_pago =?");
            nativeQuery.setParameter(1, idUsuario);
            nativeQuery.setParameter(2, fecha);
            return (float) nativeQuery.getSingleResult();
        }catch (Exception e){
            return 0;
        }

    }

}
