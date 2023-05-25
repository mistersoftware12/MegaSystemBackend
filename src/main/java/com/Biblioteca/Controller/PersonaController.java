package com.Biblioteca.Controller;


import com.Biblioteca.DTO.Persona.*;
import com.Biblioteca.Exceptions.Mensaje;
import com.Biblioteca.Service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/persona")
public class PersonaController {

    @Autowired
    private PersonaService personaService;


    @PostMapping("/registrarUsuario")
    public ResponseEntity<?> registroAlmacen(@RequestBody PersonaUsuarioRequest request){
        return new ResponseEntity<>(personaService.registrarPersona(request,1) , HttpStatus.OK);
    }

    @PutMapping("/updateUsuario")
    public ResponseEntity<?> updateUsuario(@RequestBody PersonaUsuarioRequest1 request) {
        personaService.actualizarPersona(request,1);
        return new ResponseEntity(new Mensaje("Usuario Actualizado"), HttpStatus.OK);
    }

    @GetMapping("/allUsuarios/{idEmpresa}")
    public ResponseEntity<List<PersonaUsuarioResponse>> listAllUsuarios(@PathVariable Long idEmpresa) {
        List<PersonaUsuarioResponse> users = personaService.listAllUsuarios(idEmpresa);
        return new ResponseEntity<List<PersonaUsuarioResponse>>(users, HttpStatus.OK);
    }


    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PersonaUsuarioResponse> listUsuarioByCedula(@PathVariable Long idUsuario){
        PersonaUsuarioResponse user = personaService.usuarioByCedula(idUsuario);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRequest request) throws Exception {
        PersonaUsuarioResponse response = personaService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //CLIENTE--------

    @PostMapping("/registrarCliente")
    public ResponseEntity<?> registrarCliente(@RequestBody PersonaUsuarioRequest request){
        return new ResponseEntity<>(personaService.registrarPersona(request,2) , HttpStatus.OK);
    }


    @GetMapping("/allClientes/{idEmpresa}")
    public ResponseEntity<List<PersonaUsuarioResponse>> allClientes(@PathVariable Long idEmpresa) {
        List<PersonaUsuarioResponse> users = personaService.listAllClientes(idEmpresa);
        return new ResponseEntity<List<PersonaUsuarioResponse>>(users, HttpStatus.OK);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<PersonaUsuarioResponse> listClienteByCedula(@PathVariable Long idCliente){
        PersonaUsuarioResponse user = personaService.clienteByCedula(idCliente);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/updateCliente")
    public ResponseEntity<?> updateCliente(@RequestBody PersonaUsuarioRequest1 request) {
        personaService.actualizarPersona(request,2);
        return new ResponseEntity(new Mensaje("Cliente Actualizado"), HttpStatus.OK);
    }


    ////PROVEEDOR
    @PostMapping("/registrarProveedor")
    public ResponseEntity<?> registrarProveedor(@RequestBody PersonaProveedorRequest request){
        return new ResponseEntity<>(personaService.registrarProveedor(request) , HttpStatus.OK);
    }

    @GetMapping("/allProveedores/{idEmpresa}")
    public ResponseEntity<List<PersonaProveedorResponse>> allProveedores(@PathVariable Long idEmpresa) {
        List<PersonaProveedorResponse> users = personaService.listAllProveedor(idEmpresa);
        return new ResponseEntity<List<PersonaProveedorResponse>>(users, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{idCliente}")
    public ResponseEntity<PersonaProveedorResponse> listProveedorByCedula(@PathVariable Long idCliente){
        PersonaProveedorResponse user = personaService.proveedorByCedula(idCliente);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/updateProveedor")
    public ResponseEntity<?> updateProveedor(@RequestBody PersonaProveedorResponse request) {
        personaService.actualizarProveedor(request);
        return new ResponseEntity(new Mensaje("Proveedor Actualizado"), HttpStatus.OK);
    }

}
