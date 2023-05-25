package com.Biblioteca.Service.Producto;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Producto.IngresoBajaProducto.IngresoBajaProductoRequest;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Producto.Baja.BajaProducto;
import com.Biblioteca.Models.Producto.Ingreso.IngresoProducto;
import com.Biblioteca.Models.Producto.IngresoBaja.IngresoBajaProducto;
import com.Biblioteca.Models.Producto.Producto;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Producto.Baja.BajaProductoRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IngresoBajaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    IngresoBajaProductoRepository ingresoBajaProductoRepository;

    @Autowired
    IngresoProductoRepository ingresoProductoRepository;

    @Autowired
    BajaProductoRepository bajaProductoRepository;


    public boolean regitrarIngresoBajaProdcuto (IngresoBajaProductoRequest ingresoBajaProductoRequest , int numero){

        boolean condicion = true;

        if(numero==2){
            if(countStockProducto(ingresoBajaProductoRequest.getIdProducto())>=ingresoBajaProductoRequest.getCantidad()){
                condicion = true;
            }else{
                condicion= false;
            }
        }

        if(condicion == true){
            Optional<Producto> optionalProducto = productoRepository.findById(ingresoBajaProductoRequest.getIdProducto());
            if(optionalProducto.isPresent()){
                IngresoBajaProducto newIngresoBajaProducto = new IngresoBajaProducto();
                newIngresoBajaProducto.setProducto(optionalProducto.get());
                newIngresoBajaProducto.setCantidad(ingresoBajaProductoRequest.getCantidad());
                newIngresoBajaProducto.setFechaRegistro(ingresoBajaProductoRequest.getFechaRegistro());
                newIngresoBajaProducto.setPrecioCompra(ingresoBajaProductoRequest.getPrecioCompra());

                try {
                    ingresoBajaProductoRepository.save(newIngresoBajaProducto);
                    if(numero == 1){
                        regitrarIngresoProdcuto(newIngresoBajaProducto.getId() , ingresoBajaProductoRequest);
                    }

                    if(numero == 2){
                        regitrarBajaProdcuto(newIngresoBajaProducto.getId() , ingresoBajaProductoRequest);
                    }
                    return true;
                }catch (Exception e){
                    throw new BadRequestException("No se registró el producto" +e);
                }
            }else{
                throw new BadRequestException("No existe un producto con id " +ingresoBajaProductoRequest.getIdProducto());
            }
        }else{
            throw new BadRequestException("La cantidad debe ser menor a  " +countStockProducto(ingresoBajaProductoRequest.getIdProducto()));

        }

    }


    public boolean regitrarIngresoProdcuto (Long id, IngresoBajaProductoRequest ingresoBajaProductoRequest){

        Optional<IngresoBajaProducto> optionalIngresoBajaProducto = ingresoBajaProductoRepository.findById(id);
        if(optionalIngresoBajaProducto.isPresent()){
            IngresoProducto newIngresoProducto = new IngresoProducto();
            newIngresoProducto.setIngresoBajaProducto(optionalIngresoBajaProducto.get());
            try {
                ingresoProductoRepository.save(newIngresoProducto);
                sumarStock(ingresoBajaProductoRequest.getIdProducto() , ingresoBajaProductoRequest.getCantidad());
                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró el producto" +e);
            }
        }else{
            throw new BadRequestException("No existe un producto con id " +ingresoBajaProductoRequest.getIdProducto());
        }
    }

    public boolean regitrarBajaProdcuto (Long id, IngresoBajaProductoRequest ingresoBajaProductoRequest){

        Optional<IngresoBajaProducto> optionalIngresoBajaProducto = ingresoBajaProductoRepository.findById(id);
        if(optionalIngresoBajaProducto.isPresent()){

            BajaProducto newBajaProducto = new BajaProducto();
            newBajaProducto.setIngresoBajaProducto(optionalIngresoBajaProducto.get());
            newBajaProducto.setObservacion(ingresoBajaProductoRequest.getObservacion());
            try {
                bajaProductoRepository.save(newBajaProducto);
                restarStock(ingresoBajaProductoRequest.getIdProducto() , ingresoBajaProductoRequest.getCantidad());
                return true;
            }catch (Exception e){
                throw new BadRequestException("No se registró el producto" +e);
            }
        }else{
            throw new BadRequestException("No existe un producto con id " +ingresoBajaProductoRequest.getIdProducto());
        }
    }

    public boolean sumarStock(Long idProducto , float cantidad){

        Optional<Producto> optionalProducto = productoRepository.findById(idProducto);
        if(optionalProducto.isPresent()){
            optionalProducto.get().setStock(countStockProducto(idProducto) + cantidad);
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


    /*
    public List<CategoriaRequest> listAllCategoria(Long idEmpresa) {
        List<Categoria> categorias = categoriaRepository.findAllByIdEmpresa(idEmpresa);
        return categorias.stream().map(sucursalRequest->{
            CategoriaRequest response = new CategoriaRequest();
            response.setId(sucursalRequest.getId());
            response.setNombre(sucursalRequest.getNombre());
            return response;
        }).collect(Collectors.toList());
    }

*/




    @PersistenceContext
    private EntityManager entityManager;

    public float countStockProducto(Long idProducto ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT stock FROM producto WHERE id =?");
        nativeQuery.setParameter(1, idProducto);
        return (float) nativeQuery.getSingleResult();
    }



}
