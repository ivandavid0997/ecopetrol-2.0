package com.nttdata.ecopetrol.talento.controller;

import com.nttdata.ecopetrol.talento.dto.request.LoginRqDTO;
import com.nttdata.ecopetrol.talento.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacion")
public class LoginController {

    private final LoginService loginService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRqDTO loginRequest) throws Exception {
        logger.info("Solicitud de login para usuario '{}'", loginRequest != null ? loginRequest.getUsuario() : "null");
        return loginService.login(loginRequest);
    }
}
