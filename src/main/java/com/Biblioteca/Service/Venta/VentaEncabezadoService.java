package com.Biblioteca.Service.Venta;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Venta.VentaContenidoRequest;
import com.Biblioteca.DTO.Venta.VentaEncabezadoRequest;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Proveedor;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Tipo.TipoContenido;
import com.Biblioteca.Models.Tipo.TipoPagoVenta;
import com.Biblioteca.Models.Venta.VentaContenido;
import com.Biblioteca.Models.Venta.VentaEncabezado;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.ClienteRepository;
import com.Biblioteca.Repository.Persona.UsuarioRepository;
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
import java.util.Optional;


@Slf4j
@Service
public class VentaEncabezadoService {
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

}
