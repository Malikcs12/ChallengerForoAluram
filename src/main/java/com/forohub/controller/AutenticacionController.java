package com.forohub.controller;

import com.forohub.domain.usuarios.DataAutenticacionDelUsuario;
import com.forohub.domain.usuarios.Usuario;
import com.forohub.infra.security.JWTDelToken;
import com.forohub.infra.security.Token;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Token token;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DataAutenticacionDelUsuario dataAutenticacionDelUsuario) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(dataAutenticacionDelUsuario.login(),
                dataAutenticacionDelUsuario.clave());
        var usuarioAutenticado = authenticationManager.authenticate(authenticationToken);
        var JWTtoken = token.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new JWTDelToken(JWTtoken));
    }
}