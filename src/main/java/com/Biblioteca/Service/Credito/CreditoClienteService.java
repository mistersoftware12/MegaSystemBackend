package com.Biblioteca.Service.Credito;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;
import com.Biblioteca.DTO.Credito.ContenidoCreditoClienteRequest;
import com.Biblioteca.DTO.Credito.CreditoClienteResponse;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Credito.ContenidoCreditoCliente;
import com.Biblioteca.Models.Credito.CreditoCliente;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Credito.ContenidoCreditoClienteRepository;
import com.Biblioteca.Repository.Credito.CreditoClienteRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CreditoClienteService {
    @Autowired
    private ContenidoCreditoClienteRepository contenidoCreditoClienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    CreditoClienteRepository creditoClienteRepository;


    public List<CreditoClienteResponse> listAllCategoria(Long idEmpresa, boolean estado) {
        List<CreditoCliente> creditoClientes = creditoClienteRepository.findAllByIdEmpresa(idEmpresa, estado);
        return creditoClientes.stream().map(dateRequest->{
            CreditoClienteResponse response = new CreditoClienteResponse();
            response.setId(dateRequest.getId());
            response.setFechaCompra(dateRequest.getVentaEncabezado().getFechaEmision());
            response.setSecuencia(dateRequest.getVentaEncabezado().getSecuencia());
            response.setNombreUsuario(dateRequest.getVentaEncabezado().getUsuario().getPersona().getNombres()+" "+dateRequest.getVentaEncabezado().getUsuario().getPersona().getApellidos());
            response.setCedulaCliente(dateRequest.getVentaEncabezado().getCliente().getPersona().getCedula());
            response.setNombreCliente(dateRequest.getVentaEncabezado().getCliente().getPersona().getNombres()+" "+dateRequest.getVentaEncabezado().getCliente().getPersona().getApellidos());
            response.setTelefonoCliente(dateRequest.getVentaEncabezado().getCliente().getPersona().getTelefono());
            response.setTotalVenta(dateRequest.getVentaEncabezado().getTotal());
            response.setTotalPendiente(dateRequest.getValor_pendiente());

            return response;
        }).collect(Collectors.toList());
    }

public  boolean registrarContenidoCredito(ContenidoCreditoClienteRequest contenidoCreditoClienteRequest){
        Optional<CreditoCliente> optionalCreditoCliente = creditoClienteRepository.findById(contenidoCreditoClienteRequest.getIdCreditoCliente());
        if(optionalCreditoCliente.isPresent()) {
            Optional<Usuario> optionalUsuario = usuarioRepository.findByIdCedulaEmpresa(optionalCreditoCliente.get().getVentaEncabezado().getEmpresa().getId(), contenidoCreditoClienteRequest.getCedulaUsuario());
            if (optionalUsuario.isPresent()) {

                if(optionalCreditoCliente.get().getVentaEncabezado().getTotal()>= contenidoCreditoClienteRequest.getValor() ){

                    ContenidoCreditoCliente creditoCliente = new ContenidoCreditoCliente();
                    creditoCliente.setUsuario(optionalUsuario.get());
                    creditoCliente.setCreditoCliente(optionalCreditoCliente.get());
                    creditoCliente.setValor(contenidoCreditoClienteRequest.getValor());
                    creditoCliente.setFechaPago(contenidoCreditoClienteRequest.getFechaPago());

                    try {
                        contenidoCreditoClienteRepository.save(creditoCliente);

                        float valorPendiente = optionalCreditoCliente.get().getValor_pendiente() - contenidoCreditoClienteRequest.getValor();

                        optionalCreditoCliente.get().setValor_pendiente(valorPendiente);

                        if(valorPendiente <= 0){
                            optionalCreditoCliente.get().setEstado(true);
                        }

                        try {
                            creditoClienteRepository.save(optionalCreditoCliente.get());
                        }catch (Exception e){
                            throw new BadRequestException("No actualizo el crédito , vuelva a intentar");
                        }
                        return true;
                    }catch (Exception e){
                        throw new BadRequestException("No se guardo el valor , vuelva a intentar");

                    }
                }else{
                    throw new BadRequestException("El valor debe ser menor a  " + optionalCreditoCliente.get().getVentaEncabezado().getTotal());
                }

            } else {
                throw new BadRequestException("No existe un usuario con cédula " + contenidoCreditoClienteRequest.getCedulaUsuario());
            }
        }else{
            throw new BadRequestException("No existe un credito con id "+contenidoCreditoClienteRequest.getIdCreditoCliente());
        }

}


    /*
    public boolean regitrarCategoria (CategoriaRequest categoriaRequest){

        if(countCategoria(categoriaRequest.getNombre(), categoriaRequest.getIdEmpresa()) == BigInteger.valueOf(0)) {

            Optional<Empresa> optionalEmpresa = empresaRepository.findById(categoriaRequest.getIdEmpresa());

            if(optionalEmpresa.isPresent()){
                Categoria newCategoria = new Categoria();
                newCategoria.setNombre(categoriaRequest.getNombre());
                newCategoria.setEmpresa(optionalEmpresa.get());
                try {
                    categoriaRepository.save(newCategoria);
                    return true;
                }catch (Exception e){
                    throw new BadRequestException("No se registró la sucursal" +e);
                }
            }else{
                throw new BadRequestException("No existe una empresa con id " +categoriaRequest.getIdEmpresa());
            }

        }else {
            throw new BadRequestException("Ya existe una categiria con ese nombre");
        }
    }



    public List<CategoriaRequest> listAllCategoria(Long idEmpresa) {
        List<Categoria> categorias = categoriaRepository.findAllByIdEmpresa(idEmpresa);
        return categorias.stream().map(sucursalRequest->{
            CategoriaRequest response = new CategoriaRequest();
            response.setId(sucursalRequest.getId());
            response.setNombre(sucursalRequest.getNombre());
            return response;
        }).collect(Collectors.toList());
    }

    public CategoriaRequest categoriaById(Long id){
        CategoriaRequest response = new CategoriaRequest();
        Optional<Categoria> categoriaRequest = categoriaRepository.findById(id);
        if(categoriaRequest.isPresent()){
            response.setId(categoriaRequest.get().getId());
            response.setNombre(categoriaRequest.get().getNombre());
            return response;
        }else{
            throw new BadRequestException("No existe una categoria con id seleccionado");
        }
    }


    @Transactional
    public boolean actualizarcategoria(CategoriaRequest categoriaRequest){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoriaRequest.getId());

        if(categoriaOptional.isPresent()){
            categoriaOptional.get().setNombre(categoriaRequest.getNombre());
            try{
                categoriaRepository.save(categoriaOptional.get());
                return true;
            }catch (Exception ex) {
                throw new BadRequestException("No se actualizo" + ex);
            }
        } else {
            throw new BadRequestException("No existe un evento con id "+categoriaRequest.getId() );
        }
    }


    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countCategoria(String nombre, Long idEmpresa ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM categoria WHERE nombre =? AND empresa_id =?");
        nativeQuery.setParameter(1, nombre);
        nativeQuery.setParameter(2, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();
    }
*/

}
