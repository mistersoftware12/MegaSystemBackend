package com.Biblioteca.Service.Produccion;

import com.Biblioteca.DTO.Extra.IdResponse;
import com.Biblioteca.DTO.Produccion.ContenidoProduccionRequest;
import com.Biblioteca.DTO.Produccion.ContenidoProduccionResponse;
import com.Biblioteca.DTO.Produccion.ProduccionRequest;
import com.Biblioteca.DTO.Produccion.ProduccionResponse;
import com.Biblioteca.DTO.Producto.ProductoRequest;
import com.Biblioteca.DTO.Producto.ProductoResponse;
import com.Biblioteca.DTO.Producto.ProductoResponse1;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Proveedor;
import com.Biblioteca.Models.Produccion.ContenidoProduccion;
import com.Biblioteca.Models.Produccion.Produccion;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import com.Biblioteca.Models.Producto.Producto;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.ProveedorRepository;
import com.Biblioteca.Repository.Produccion.ContenidoProduccionRepository;
import com.Biblioteca.Repository.Produccion.ProduccionRepository;
import com.Biblioteca.Repository.Producto.Ingreso.IngresoProductoRepository;
import com.Biblioteca.Repository.Producto.IngresoBajaProductoRepository;
import com.Biblioteca.Repository.Producto.ProductoRepository;
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
public class ProduccionService {
    @Autowired
    private ContenidoProduccionRepository contenidoProduccionRepository;
    @Autowired
    private IngresoProductoRepository ingresoProductoRepository;
    @Autowired
    private IngresoBajaProductoRepository ingresoBajaProductoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    ProveedorRepository proveedorRepository;
    @Autowired
    ProduccionRepository produccionRepository;


    public IdResponse regitrarProduccion (ProduccionRequest produccionRequest){

        if(countProduccion(produccionRequest.getNombre(), produccionRequest.getIdEmpresa()) == BigInteger.valueOf(0)) {

            Optional<Empresa> optionalEmpresa = empresaRepository.findById(produccionRequest.getIdEmpresa());

            if(optionalEmpresa.isPresent()){

                        Produccion newProducto = new Produccion();
                        newProducto.setEmpresa(optionalEmpresa.get());
                        newProducto.setNombre(produccionRequest.getNombre());
                        newProducto.setCodigoBarra(produccionRequest.getCodigoBarra());
                        newProducto.setIva(produccionRequest.getIva());
                        newProducto.setPrecioVenta(produccionRequest.getPrecioVenta());
                        newProducto.setPrecioCompra(produccionRequest.getPrecioCompra());
                        try {
                            produccionRepository.save(newProducto);
                         return new IdResponse(newProducto.getId());
                        }catch (Exception e){
                            throw new BadRequestException("No se registró el producto" +e);
                        }

            }else{
                throw new BadRequestException("No existe una empresa con id " +produccionRequest.getIdEmpresa());
            }


        }else {
            throw new BadRequestException("Ya existe un producto  con ese nombre");
        }
    }




    public List<ProduccionResponse> listAllProduccion(Long idEmpresa) {
        List<Produccion> productos = produccionRepository.findAllByIdEmpresa(idEmpresa);
        return productos.stream().map(sucursalRequest->{
            ProduccionResponse response = new ProduccionResponse();
            response.setId(sucursalRequest.getId());
            response.setNombre(sucursalRequest.getNombre());
            response.setIva(sucursalRequest.getIva());
            response.setPrecioVenta(sucursalRequest.getPrecioVenta());
            response.setCodigoBarra(sucursalRequest.getCodigoBarra());

            return response;
        }).collect(Collectors.toList());
    }

    public ProduccionResponse produccionById(Long id){
        ProduccionResponse response = new ProduccionResponse();
        Optional<Produccion> productoRequest = produccionRepository.findById(id);
        if(productoRequest.isPresent()){
            response.setId(productoRequest.get().getId());
            response.setNombre(productoRequest.get().getNombre());
            response.setCodigoBarra(productoRequest.get().getCodigoBarra());
            response.setIva(productoRequest.get().getIva());
            response.setPrecioVenta(productoRequest.get().getPrecioVenta());
            response.setPrecioCompra(productoRequest.get().getPrecioCompra());
            response.setListaContenidoProduccion(listAllContenidoProduccion(productoRequest.get().getId()));
            return response;
        }else{
            throw new BadRequestException("No existe una categoria con id seleccionado");
        }
    }

    public ProduccionResponse produccionCodigoBarra(Long idEmpresa , String codigoBarra){
        ProduccionResponse response = new ProduccionResponse();
        Optional<Produccion> productoRequest = produccionRepository.findAllByIdCodigoEmpresa(idEmpresa, codigoBarra);
        if(productoRequest.isPresent()){
            response.setId(productoRequest.get().getId());
            response.setNombre(productoRequest.get().getNombre());
            response.setCodigoBarra(productoRequest.get().getCodigoBarra());
            response.setIva(productoRequest.get().getIva());
            response.setPrecioCompra(productoRequest.get().getPrecioCompra());
            System.out.println(productoRequest.get().getPrecioCompra());
            response.setPrecioVenta(productoRequest.get().getPrecioVenta());
            //response.setListaContenidoProduccion(listAllContenidoProduccion(productoRequest.get().getId()));
            response.setCodigoBarra(productoRequest.get().getCodigoBarra());
            return response;
        }else{
            throw new BadRequestException("No existe una categoria con id seleccionado");
        }
    }



    @Transactional
    public boolean actualizarproduccion(ProduccionRequest produccionRequest){


        Optional<Produccion> optionalProducto = produccionRepository.findById(produccionRequest.getId());
        if(optionalProducto.isPresent()){

                    optionalProducto.get().setNombre(produccionRequest.getNombre());
                    optionalProducto.get().setCodigoBarra(produccionRequest.getCodigoBarra());
                    optionalProducto.get().setIva(produccionRequest.getIva());
                    optionalProducto.get().setPrecioCompra(produccionRequest.getPrecioCompra());
                    optionalProducto.get().setPrecioVenta(produccionRequest.getPrecioVenta());

                    try{
                        produccionRepository.save(optionalProducto.get());
                        return true;
                    }catch (Exception ex) {
                        throw new BadRequestException("No se actualizo" + ex);
                    }

        }else{
            throw new BadRequestException("No existe una producto con id " +produccionRequest.getId());
        }


    }



    //////////////////////////////////////PRODUCCION////////////////////////

    public boolean regitrarContenidoProduccion (ContenidoProduccionRequest contenidoProduccionRequest){

            Optional<Produccion> optionalProduccion = produccionRepository.findById(contenidoProduccionRequest.getIdProduccion());
            if(optionalProduccion.isPresent()){

                Optional<Producto> optionalProducto = productoRepository.findById(contenidoProduccionRequest.getIdProducto());
                if(optionalProducto.isPresent()){

                    ContenidoProduccion contenidoProduccion = new ContenidoProduccion();

                    contenidoProduccion.setProduccion(optionalProduccion.get());
                    contenidoProduccion.setProducto(optionalProducto.get());
                  contenidoProduccion.setCantidad(contenidoProduccionRequest.getCantidad());
                    try {
                        contenidoProduccionRepository.save(contenidoProduccion);
                        return true;
                    }catch (Exception e){
                        throw new BadRequestException("No se registró el producto" +e);
                    }

                }else{
                    throw new BadRequestException("No existe un producto con id " +contenidoProduccionRequest.getIdProducto());

                }

            }else{
                throw new BadRequestException("No existe una produccion con id " +contenidoProduccionRequest.getIdProduccion());
            }


    }

    public List<ContenidoProduccionResponse> listAllContenidoProduccion(Long idProduccion) {
        List<ContenidoProduccion> productos = contenidoProduccionRepository.findAllByIdEmpresa(idProduccion);
        return productos.stream().map(sucursalRequest->{
            ContenidoProduccionResponse response = new ContenidoProduccionResponse();
            response.setId(sucursalRequest.getId());
            response.setNombre(sucursalRequest.getProducto().getNombre());
            response.setCantidad(sucursalRequest.getCantidad());
            response.setIdProduccion(sucursalRequest.getProduccion().getId());
            response.setIdProducto(sucursalRequest.getProducto().getId());
            response.setPrecioCompra(sucursalRequest.getProducto().getPrecioCompra());
            response.setPrecioVenta(sucursalRequest.getProducto().getPrecioVenta());

            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean actualizarContenidoproduccion(ContenidoProduccionRequest contenidoProduccionRequest){


        Optional<ContenidoProduccion> optionalProducto = contenidoProduccionRepository.findById(contenidoProduccionRequest.getId());
        if(optionalProducto.isPresent()){

            optionalProducto.get().setCantidad(contenidoProduccionRequest.getCantidad());

            try{
                contenidoProduccionRepository.save(optionalProducto.get());
                return true;
            }catch (Exception ex) {
                throw new BadRequestException("No se actualizo" + ex);
            }

        }else{
            throw new BadRequestException("No existe una producto con id " +contenidoProduccionRequest.getId());
        }


    }

    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countProduccion(String nombre, Long idEmpresa ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM produccion WHERE nombre =? AND empresa_id =?");
        nativeQuery.setParameter(1, nombre);
        nativeQuery.setParameter(2, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();
    }


}
