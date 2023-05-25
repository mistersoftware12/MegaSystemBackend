package com.Biblioteca.Service;

import com.Biblioteca.DTO.Persona.*;
import com.Biblioteca.Exceptions.BadRequestException;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Cliente;
import com.Biblioteca.Models.Persona.Persona;
import com.Biblioteca.Models.Persona.Proveedor;
import com.Biblioteca.Models.Persona.Usuario;
import com.Biblioteca.Models.Roles.Roles;
import com.Biblioteca.Repository.Empresa.EmpresaRepository;
import com.Biblioteca.Repository.Persona.ClienteRepository;
import com.Biblioteca.Repository.Persona.PersonaRepository;
import com.Biblioteca.Repository.Persona.ProveedorRepository;
import com.Biblioteca.Repository.Persona.UsuarioRepository;
import com.Biblioteca.Repository.RolesRepository;
import com.Biblioteca.Security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonaService implements UserDetailsService {
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public Long registrarPersona(PersonaUsuarioRequest personaUsuarioRequest, int numero) {

        Boolean estado = false;

        if (countUsuario(personaUsuarioRequest.getCedula(), personaUsuarioRequest.getIdEmpresa()) == BigInteger.valueOf(0)) {
            estado = true;
        } else {
            estado = false;
        }

        if (estado == true) {
            Optional<Empresa> optionalEmpresa = empresaRepository.findById(personaUsuarioRequest.getIdEmpresa());
            if (optionalEmpresa.isPresent()) {

                Persona newPersona = new Persona();
                newPersona.setEmpresa(optionalEmpresa.get());
                newPersona.setCedula(personaUsuarioRequest.getCedula());
                newPersona.setApellidos(personaUsuarioRequest.getApellidos());
                newPersona.setNombres(personaUsuarioRequest.getNombres());
                newPersona.setEmail(personaUsuarioRequest.getEmail());
                newPersona.setTelefono(personaUsuarioRequest.getTelefono());
                newPersona.setFechaNacimiento(personaUsuarioRequest.getFechaNacimiento());
                try {
                    personaRepository.save(newPersona);

                    if (numero == 1) {
                        registrarUsuario(newPersona.getId(), personaUsuarioRequest);
                    }

                    if(numero == 2){
                        registrarCliente(newPersona.getId(), personaUsuarioRequest);
                    }
                    return newPersona.getId();
                } catch (Exception e) {
                    throw new BadRequestException("No se registró la proforma" + e);
                }
            } else {
                throw new BadRequestException("No existe una empresa con id " + personaUsuarioRequest.getIdEmpresa());
            }
        } else {
            throw new BadRequestException("Ya existe una persona registrado con esa cédula");
        }


    }

    public Long registrarUsuario(Long idPersona, PersonaUsuarioRequest personaUsuarioRequest) {
        Optional<Persona> optionalPersona = personaRepository.findById(idPersona);
        if (optionalPersona.isPresent()) {
            Optional<Roles> optionalRoles = rolesRepository.findById(personaUsuarioRequest.getIdRol());
            if (optionalRoles.isPresent()) {
                Usuario newUsuario = new Usuario();
                newUsuario.setPersona(optionalPersona.get());
                newUsuario.setRoles(optionalRoles.get());
                newUsuario.setClave(personaUsuarioRequest.getClave());

                try {
                    usuarioRepository.save(newUsuario);
                    return newUsuario.getId();
                } catch (Exception e) {
                    throw new BadRequestException("No se registró la proforma" + e);
                }
            } else {
                throw new BadRequestException("No existe un rol con id " + personaUsuarioRequest.getIdRol());
            }
        } else {
            throw new BadRequestException("No existe una persona con id " + idPersona);
        }


    }

    public Long registrarCliente(Long idPersona, PersonaUsuarioRequest personaUsuarioRequest) {
        Optional<Persona> optionalPersona = personaRepository.findById(idPersona);
        if (optionalPersona.isPresent()) {

            Cliente newCliente = new Cliente();
            newCliente.setPersona(optionalPersona.get());
                try {
                    clienteRepository.save(newCliente);
                    return newCliente.getId();
                } catch (Exception e) {
                    throw new BadRequestException("No se registró la proforma" + e);
                }

        } else {
            throw new BadRequestException("No existe una persona con id " + idPersona);
        }


    }

    public List<PersonaUsuarioResponse> listAllUsuarios(Long idEmpresa){
        List<Usuario> usuarios = usuarioRepository.findAllByIdEmpresa(idEmpresa);
        return usuarios.stream().map(usuarioRequest->{
            PersonaUsuarioResponse pcr = new PersonaUsuarioResponse();
            pcr.setId(usuarioRequest.getPersona().getId());
            pcr.setIdUsuario(usuarioRequest.getId());
            pcr.setCedula(usuarioRequest.getPersona().getCedula());
            pcr.setNombres(usuarioRequest.getPersona().getNombres());
            pcr.setApellidos(usuarioRequest.getPersona().getApellidos());
            pcr.setTelefono(usuarioRequest.getPersona().getTelefono());
            pcr.setEmail(usuarioRequest.getPersona().getEmail());
            pcr.setFechaNacimiento(usuarioRequest.getPersona().getFechaNacimiento());
            pcr.setIdRol(usuarioRequest.getRoles().getId());
            pcr.setNombreRol(usuarioRequest.getRoles().getDescripcion());
            return pcr;
        }).collect(Collectors.toList());
    }

    public List<PersonaUsuarioResponse> listAllClientes(Long idEmpresa){
        List<Cliente> clientes = clienteRepository.findAllByIdEmpresa(idEmpresa);

        return clientes.stream().map(usuarioRequest->{
            PersonaUsuarioResponse pcr = new PersonaUsuarioResponse();
            pcr.setId(usuarioRequest.getPersona().getId());
            pcr.setIdUsuario(usuarioRequest.getId());
            pcr.setCedula(usuarioRequest.getPersona().getCedula());
            pcr.setNombres(usuarioRequest.getPersona().getNombres());
            pcr.setApellidos(usuarioRequest.getPersona().getApellidos());
            pcr.setTelefono(usuarioRequest.getPersona().getTelefono());
            pcr.setEmail(usuarioRequest.getPersona().getEmail());
            pcr.setFechaNacimiento(usuarioRequest.getPersona().getFechaNacimiento());
            return pcr;
        }).collect(Collectors.toList());
    }

    public List<PersonaProveedorResponse> listAllProveedor(Long idEmpresa){
        List<Proveedor> proveedors =  proveedorRepository.findAllByIdEmpresa(idEmpresa);

        return proveedors.stream().map(provRequest->{
            PersonaProveedorResponse pcr = new PersonaProveedorResponse();
            pcr.setId(provRequest.getId());
            pcr.setPropietario(provRequest.getPropietario());
            pcr.setNombreComercial(provRequest.getNombreComercial());
            pcr.setTelefono(provRequest.getTelefono());
            pcr.setEmail(provRequest.getEmail());

            return pcr;
        }).collect(Collectors.toList());
    }

    public PersonaUsuarioResponse usuarioByCedula(Long id){

        PersonaUsuarioResponse response = new PersonaUsuarioResponse();
        Optional<Usuario> usuarioRequest = usuarioRepository.findById(id);

        if(usuarioRequest.isPresent()){
            response.setId(usuarioRequest.get().getId());
            response.setIdPersona(usuarioRequest.get().getPersona().getId());
            response.setIdUsuario(usuarioRequest.get().getId());
            response.setCedula(usuarioRequest.get().getPersona().getCedula());
            response.setApellidos(usuarioRequest.get().getPersona().getApellidos());
            response.setNombres(usuarioRequest.get().getPersona().getNombres());
            response.setFechaNacimiento(usuarioRequest.get().getPersona().getFechaNacimiento());
            response.setEmail(usuarioRequest.get().getPersona().getEmail());
            response.setTelefono(usuarioRequest.get().getPersona().getTelefono());
            response.setIdRol(usuarioRequest.get().getRoles().getId());

            return response;
        }else{
            throw new BadRequestException("No existe un usaurio con id seleccionado");
        }


    }

    public PersonaUsuarioResponse clienteByCedula(Long id){

        PersonaUsuarioResponse response = new PersonaUsuarioResponse();
        Optional<Cliente> usuarioRequest = clienteRepository.findById(id);

        if(usuarioRequest.isPresent()){
            response.setId(usuarioRequest.get().getId());
            response.setIdPersona(usuarioRequest.get().getPersona().getId());
            response.setIdUsuario(usuarioRequest.get().getId());
            response.setCedula(usuarioRequest.get().getPersona().getCedula());
            response.setApellidos(usuarioRequest.get().getPersona().getApellidos());
            response.setNombres(usuarioRequest.get().getPersona().getNombres());
            response.setFechaNacimiento(usuarioRequest.get().getPersona().getFechaNacimiento());
            response.setEmail(usuarioRequest.get().getPersona().getEmail());
            response.setTelefono(usuarioRequest.get().getPersona().getTelefono());

            return response;
        }else{
            throw new BadRequestException("No existe un usaurio con id seleccionado");
        }


    }

    public PersonaProveedorResponse proveedorByCedula(Long id){
        PersonaProveedorResponse response = new PersonaProveedorResponse();
        Optional<Proveedor> proveedorRequest = proveedorRepository.findById(id);
        if(proveedorRequest.isPresent()){
            response.setId(proveedorRequest.get().getId());
            response.setEmail(proveedorRequest.get().getEmail());
            response.setTelefono(proveedorRequest.get().getTelefono());
            response.setPropietario(proveedorRequest.get().getPropietario());
            response.setNombreComercial(proveedorRequest.get().getNombreComercial());
            return response;
        }else{
            throw new BadRequestException("No existe un usaurio con id seleccionado");
        }
    }


    @Transactional
    public boolean actualizarPersona(PersonaUsuarioRequest1 personaUsuarioRequest , int numero ){

        Optional<Persona> persona = personaRepository.findById(personaUsuarioRequest.getIdPersona());
        if(persona.isPresent()){

            persona.get().setNombres(personaUsuarioRequest.getNombres());
            persona.get().setApellidos(personaUsuarioRequest.getApellidos());
            persona.get().setEmail(personaUsuarioRequest.getEmail());
            persona.get().setFechaNacimiento(personaUsuarioRequest.getFechaNacimiento());
            persona.get().setTelefono(personaUsuarioRequest.getTelefono());

            try{
                personaRepository.save(persona.get());
                if(numero==1){
                    actualizarUsuario(personaUsuarioRequest);
                }
                return true;
            }catch (Exception ex) {
                throw new BadRequestException("No se actualizo" + ex);
            }

        } else {
            throw new BadRequestException("No existe una persona  con id "+personaUsuarioRequest.getId() );
        }
    }

    @Transactional
    public boolean actualizarUsuario(PersonaUsuarioRequest1 personaUsuarioRequest ){

        Optional<Persona> optionalPersona = personaRepository.findById(personaUsuarioRequest.getIdPersona());
        if(optionalPersona.isPresent()){

            Optional<Roles> optionalRoles = rolesRepository.findById(personaUsuarioRequest.getIdRol());

            if(optionalRoles.isPresent()){

                Optional<Usuario> usuario = usuarioRepository.findById(personaUsuarioRequest.getId());

                if(usuario.isPresent()){
                    usuario.get().setClave(personaUsuarioRequest.getClave());
                    usuario.get().setRoles(optionalRoles.get());
                    try{
                        usuarioRepository.save(usuario.get());
                        return true;
                    }catch (Exception ex) {
                        throw new BadRequestException("No se actualizo" + ex);
                    }
                }else{
                    throw new BadRequestException("No existe una usuario  con id "+personaUsuarioRequest.getId() );
                }
            }else{
                throw new BadRequestException("No existe unarol  con id "+personaUsuarioRequest.getIdRol() );
            }

        } else {
            throw new BadRequestException("No existe una persona  con id "+personaUsuarioRequest.getIdPersona() );
        }
    }

    @Transactional
    public boolean actualizarProveedor(PersonaProveedorResponse personaProveedorResponse){


        Optional<Proveedor> proveedor = proveedorRepository.findById(personaProveedorResponse.getId());
        if(proveedor.isPresent()){
            proveedor.get().setPropietario(personaProveedorResponse.getPropietario());
            proveedor.get().setNombreComercial(personaProveedorResponse.getNombreComercial());
            proveedor.get().setEmail(personaProveedorResponse.getEmail());
            proveedor.get().setTelefono(personaProveedorResponse.getTelefono());
            try{
                proveedorRepository.save(proveedor.get());
                return true;
            }catch (Exception ex) {
                throw new BadRequestException("No se actualizo" + ex);
            }

        } else {
            throw new BadRequestException("No existe un proveedor  con id "+personaProveedorResponse.getId() );
        }
    }

    public PersonaUsuarioResponse login (UsuarioRequest usuarioRequest) throws Exception {
        Optional<Persona> optional = personaRepository.findByCedula(usuarioRequest.getCedula());
        if(optional.isPresent()){
            Optional<Usuario> usuarioOptional= usuarioRepository.findByPersona(optional.get());
            if(usuarioOptional.isPresent()){
                if(usuarioRequest.getClave().equals(usuarioOptional.get().getClave())){
                    return new PersonaUsuarioResponse(optional.get().getId(),optional.get().getCedula(),
                            optional.get().getApellidos(), optional.get().getNombres(), optional.get().getEmail(),
                            optional.get().getTelefono(), usuarioOptional.get().getClave(), usuarioOptional.get().getRoles().getId(),
                            generateTokenLogin(usuarioRequest) , usuarioOptional.get().getPersona().getEmpresa().getId());
                }else{
                    throw new BadRequestException("Contraseña incorrecta para email: " + usuarioRequest.getCedula());
                }
            }else{
                log.info("CEDULA NO EXISTE");
                throw new BadRequestException("Usuario no registrado como usuario");
            }
        }else{
            log.info("CEDULA NO EXISTE");
            throw new BadRequestException("Usuario no registrado");
        }
    }
        @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Persona> usuario = personaRepository.findByCedula(email);
        return new org.springframework.security.core.userdetails.User(usuario.get().getCedula(), usuario.get().getCedula(), new ArrayList<>());
    }

    public String generateTokenLogin(UsuarioRequest userRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getCedula(), userRequest.getCedula())
            );
        } catch (Exception ex) {
            log.error("INVALID: error al generar token en login de usuario con cedula: {}", userRequest.getCedula());
            throw new Exception("INAVALID");
        }
        return jwtUtil.generateToken(userRequest.getCedula());
    }

    public String generateTokenSignUp(PersonaUsuarioRequest registerRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registerRequest.getCedula(), registerRequest.getCedula())
            );
        } catch (Exception ex) {
            log.error("INVALID: error al generar token en signup de usuario con cedula: {}", registerRequest.getCedula());
            throw new BadRequestException("INAVALID");
        }
        return jwtUtil.generateToken(registerRequest.getCedula());
    }

    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger countUsuario(String cedula, Long idEmpresa ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(p.id) FROM usuario u INNER JOIN persona p ON u.persona_id = p.id WHERE p.cedula =?");
        nativeQuery.setParameter(1, cedula);
        return (BigInteger) nativeQuery.getSingleResult();
    }

    public BigInteger countProveedor(String nombre, Long idEmpresa ) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM proveedor WHERE nombre_comercial =? AND empresa_id =?");
        nativeQuery.setParameter(1, nombre);
        nativeQuery.setParameter(2, idEmpresa);
        return (BigInteger) nativeQuery.getSingleResult();
    }





    //PROVEEDOR

    public Long registrarProveedor(PersonaProveedorRequest personaProveedorRequest) {

        if (countProveedor(personaProveedorRequest.getNombreComercial(), personaProveedorRequest.getIdEmpresa()) == BigInteger.valueOf(0)) {
            Optional<Empresa> optionalEmpresa = empresaRepository.findById(personaProveedorRequest.getIdEmpresa());
            if (optionalEmpresa.isPresent()) {
                Proveedor newProveedor = new Proveedor();
                newProveedor.setNombreComercial(personaProveedorRequest.getNombreComercial());
                newProveedor.setEmpresa(optionalEmpresa.get());
                newProveedor.setPropietario(personaProveedorRequest.getPropietario());
                newProveedor.setTelefono(personaProveedorRequest.getTelefono());
                newProveedor.setEmail(personaProveedorRequest.getEmail());
                try {
                    proveedorRepository.save(newProveedor);
                    return newProveedor.getId();
                } catch (Exception e) {
                    throw new BadRequestException("No se registró la proforma" + e);
                }
            } else {
                throw new BadRequestException("No existe una empresa con id " + personaProveedorRequest.getIdEmpresa());
            }


        } else {
            throw new BadRequestException("Ya existe un nombre Comercial registrado con para su empresa");
        }

    }


}
