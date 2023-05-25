package com.Biblioteca.Service.Categoria;

import com.Biblioteca.DTO.Categoria.CategoriaRequest;

import com.Biblioteca.DTO.Persona.PersonaProveedorResponse;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Proveedor;
import com.Biblioteca.Repository.Categoria.CategoriaRepository;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
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
public class CategoriaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;


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


}
