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

}
