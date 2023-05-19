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


    @PutMapping("/updateUsuario")
    public ResponseEntity<?> updateUsuario(@RequestBody PersonaUsuarioRequest request) {
        personaService.updateUsuario(request);
        return new ResponseEntity(new Mensaje("Usuario Actualizado"), HttpStatus.OK);
    }

    @PostMapping("/registroUsuario")
    public ResponseEntity<PersonaUsuarioResponse> registroUsuario(@RequestBody PersonaUsuarioRequest personaRequest) throws Exception {
        PersonaUsuarioResponse personaResponse = personaService.registrarUsuario(personaRequest);

        if (personaResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(personaResponse, HttpStatus.CREATED);
    }




    @GetMapping("/allUsuarios")
    public ResponseEntity<List<PersonaUsuarioResponse>> listAllUsuarios() {
        List<PersonaUsuarioResponse> users = personaService.listAllUsuarios();
        return new ResponseEntity<List<PersonaUsuarioResponse>>(users, HttpStatus.OK);
    }

    @GetMapping("/usuario/{cedula}")
    public ResponseEntity<PersonaUsuarioResponse> listUsuarioByCedula(@PathVariable String cedula){
        PersonaUsuarioResponse user = personaService.usuarioByCedula(cedula);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRequest request) throws Exception {
        PersonaUsuarioResponse response = personaService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
