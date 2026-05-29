package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.dto.request.LoginRqDTO;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?> login(LoginRqDTO loginRequest) throws Exception;
}
