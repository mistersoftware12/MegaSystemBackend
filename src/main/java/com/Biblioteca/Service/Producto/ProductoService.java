package com.Biblioteca.Service.Producto;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Producto.IngresoBajaProducto.IngresoBajaProductoRequest;
import com.Biblioteca.DTO.Producto.ProductoRequest;
import com.Biblioteca.DTO.Producto.ProductoResponse;
import com.Biblioteca.DTO.Producto.ProductoResponse1;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Proveedor;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import com.Biblioteca.Models.Producto.Producto;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.ProveedorRepository;
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
public class ProductoService {
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


    public boolean regitrarProducto (ProductoRequest productoRequest){

        if(countProducto(productoRequest.getNombre(), productoRequest.getIdEmpresa()) == BigInteger.valueOf(0)) {

            Optional<Empresa> optionalEmpresa = empresaRepository.findById(productoRequest.getIdEmpresa());

            if(optionalEmpresa.isPresent()){

                Optional<Categoria> optionalCategoria = categoriaRepository.findById(productoRequest.getIdCategoria());

                if(optionalCategoria.isPresent()){

                    Optional<Proveedor> optionalProveedor = proveedorRepository.findById(productoRequest.getIdProveedor());

                    if(optionalProveedor.isPresent()){

                        Producto newProducto = new Producto();
                        newProducto.setEmpresa(optionalEmpresa.get());
                        newProducto.setCategoria(optionalCategoria.get());
                        newProducto.setProveedor(optionalProveedor.get());
                        newProducto.setNombre(productoRequest.getNombre());
                        newProducto.setCodigoBarra(productoRequest.getCodigoBarra());
                        newProducto.setIva(productoRequest.getIva());
                        newProducto.setStock(productoRequest.getStock());
                        newProducto.setPrecioCompra(productoRequest.getPrecioCompra());
                        newProducto.setPrecioVenta(productoRequest.getPrecioVenta());
                        try {
                            productoRepository.save(newProducto);
                            regitrarIngresoBajaProducto(newProducto.getId(), productoRequest.getStock() , productoRequest.getFechaPrimeraCompra(),productoRequest.getPrecioPrimeraCompra());
                            return true;
                        }catch (Exception e){
                            throw new BadRequestException("No se registró el producto" +e);
                        }
                    }else{
                        throw new BadRequestException("No existe una proveeedor con id " +productoRequest.getIdProveedor());
                    }
                }else{
                    throw new BadRequestException("No existe una categoria con id " +productoRequest.getIdCategoria());
                }
            }else{
                throw new BadRequestException("No existe una empresa con id " +productoRequest.getIdEmpresa());
            }


        }else {
            throw new BadRequestException("Ya existe un producto  con ese nombre");
        }
    }


    public boolean regitrarIngresoBajaProducto (Long idProducto , float cantidad , Date fecha , float precioCompra){
        Optional<Producto> optionalProducto = productoRepository.findById(idProducto);
        if(optionalProducto.isPresent()){
            IngresoBajaProducto newIngresoBajaProducto = new IngresoBajaProducto();
            newIngresoBajaProducto.setProducto(optionalProducto.get());
            newIngresoBajaProducto.setCantidad(cantidad);
            newIngresoBajaProducto.setFechaRegistro(fecha);
            newIngresoBajaProducto.setPrecioCompra(precioCompra);

            try {
                ingresoBajaProductoRepository.save(newIngresoBajaProducto);
                regitrarIngresoProdcuto(newIngresoBajaProducto.getId());

                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró el producto" +e);
            }
        }else{
            throw new BadRequestException("No existe un producto con id " +idProducto);
        }
    }


    public boolean regitrarIngresoProdcuto (Long id){

        Optional<IngresoBajaProducto> optionalIngresoBajaProducto = ingresoBajaProductoRepository.findById(id);
        if(optionalIngresoBajaProducto.isPresent()){
            IngresoProducto newIngresoProducto = new IngresoProducto();
            newIngresoProducto.setIngresoBajaProducto(optionalIngresoBajaProducto.get());
            try {
                ingresoProductoRepository.save(newIngresoProducto);
                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró el producto" +e);
            }
        }else{
            throw new BadRequestException("No existe un producto con id ");
        }
    }


    public List<ProductoResponse1> listAllProductos(Long idEmpresa) {
        List<Producto> productos = productoRepository.findAllByIdEmpresa(idEmpresa);
        return productos.stream().map(sucursalRequest->{
            ProductoResponse1 response = new ProductoResponse1();
            response.setId(sucursalRequest.getId());
            response.setNombre(sucursalRequest.getNombre());
            response.setStock(sucursalRequest.getStock());
            response.setPrecioCompra(sucursalRequest.getPrecioCompra());
            response.setPrecioVenta(sucursalRequest.getPrecioVenta());
            response.setNombreCategoria(sucursalRequest.getCategoria().getNombre());
            response.setNombreProveedor(sucursalRequest.getProveedor().getPropietario());
            response.setCodigoBarra(sucursalRequest.getCodigoBarra());
            return response;
        }).collect(Collectors.toList());
    }

    public ProductoResponse productoById(Long id){
        ProductoResponse response = new ProductoResponse();
        Optional<Producto> productoRequest = productoRepository.findById(id);
        if(productoRequest.isPresent()){
            response.setId(productoRequest.get().getId());
            response.setNombre(productoRequest.get().getNombre());
            response.setCodigoBarra(productoRequest.get().getCodigoBarra());
            response.setStock(productoRequest.get().getStock());
            response.setIva(productoRequest.get().getIva());
            response.setPrecioCompra(productoRequest.get().getPrecioCompra());
            response.setPrecioVenta(productoRequest.get().getPrecioVenta());
            response.setIdCategoria(productoRequest.get().getCategoria().getId());
            response.setIdProveedor(productoRequest.get().getProveedor().getId());

            return response;
        }else{
            throw new BadRequestException("No existe una categoria con id seleccionado");
        }
    }

    public ProductoResponse productoByBarra(Long idEmpresa , String codigoBarra){
        ProductoResponse response = new ProductoResponse();
        Optional<Producto> productoRequest = productoRepository.findAllByIdCodigoEmpresa(idEmpresa , codigoBarra);
        if(productoRequest.isPresent()){
            response.setId(productoRequest.get().getId());
            response.setNombre(productoRequest.get().getNombre());
            response.setCodigoBarra(productoRequest.get().getCodigoBarra());
            response.setStock(productoRequest.get().getStock());
            response.setIva(productoRequest.get().getIva());
            response.setPrecioCompra(productoRequest.get().getPrecioCompra());
            response.setPrecioVenta(productoRequest.get().getPrecioVenta());
            response.setIdCategoria(productoRequest.get().getCategoria().getId());
            response.setIdProveedor(productoRequest.get().getProveedor().getId());

            return response;
        }else{
            throw new BadRequestException("No existe una categoria con id seleccionado");
        }
    }


    @Transactional
    public boolean actualizarproducto(ProductoRequest productoRequest){

        Optional<Producto> optionalProducto = productoRepository.findById(productoRequest.getId());

        if(optionalProducto.isPresent()){
            Optional<Categoria> optionalCategoria = categoriaRepository.findById(productoRequest.getIdCategoria());

            if(optionalCategoria.isPresent()){

                Optional<Proveedor> optionalProveedor = proveedorRepository.findById(productoRequest.getIdProveedor());

                if(optionalProveedor.isPresent()){

                    optionalProducto.get().setCategoria(optionalCategoria.get());
                    optionalProducto.get().setProveedor(optionalProveedor.get());
                    optionalProducto.get().setNombre(productoRequest.getNombre());
                    optionalProducto.get().setCodigoBarra(productoRequest.getCodigoBarra());
                    optionalProducto.get().setIva(productoRequest.getIva());
                    optionalProducto.get().setPrecioCompra(productoRequest.getPrecioCompra());
                    optionalProducto.get().setPrecioVenta(productoRequest.getPrecioVenta());

                    try{
                        productoRepository.save(optionalProducto.get());
                        return true;
                    }catch (Exception ex) {
                        throw new BadRequestException("No se actualizo" + ex);
                    }

                }else{
                    throw new BadRequestException("No existe una proveeedor con id " +productoRequest.getIdProveedor());
                }
            }else{
                throw new BadRequestException("No existe una categoria con id " +productoRequest.getIdCategoria());
            }
        }else{
            throw new BadRequestException("No existe una producto con id " +productoRequest.getId());
        }


    }


    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countProducto(String nombre, Long idEmpresa ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM producto WHERE nombre =? AND empresa_id =?");
        nativeQuery.setParameter(1, nombre);
        nativeQuery.setParameter(2, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();
    }


}
