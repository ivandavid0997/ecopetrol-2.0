package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.dto.request.LoginRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.LoginRsDTO;
import com.nttdata.ecopetrol.talento.enums.CodigoError;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.security.JwtUtil;
import com.nttdata.ecopetrol.talento.services.LoginService;
import com.nttdata.ecopetrol.talento.utils.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private static final int MAX_INTENTOS = 3;
    private static final int BLOQUEO_MINUTOS = 15;

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final AesUtil aesUtil;

    public LoginServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, AesUtil aesUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.aesUtil = aesUtil;
    }

    @Override
    public ResponseEntity<?> login(LoginRqDTO loginRequest) throws Exception {
        if (loginRequest == null || loginRequest.getUsuario() == null) {
            CodigoError ce = CodigoError.LOGIN_FALLIDO;
            MDC.put("error_code", ce.getCodigo());
            MDC.put("usuario", "anonimo");
            logger.warn("Login fallido: request o usuario nulo");
            MDC.remove("error_code");
            MDC.remove("usuario");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("usuario", "anonimo", "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        String cleanedUsuario = loginRequest.getUsuario().trim().toLowerCase();
        MDC.put("usuario", cleanedUsuario);
        Usuario usuario = usuarioRepository.findByUsuario(cleanedUsuario);

        if (usuario == null) {
            CodigoError ce = CodigoError.USUARIO_NO_ENCONTRADO;
            MDC.put("error_code", ce.getCodigo());
            logger.warn("Login fallido: usuario '{}' no encontrado", cleanedUsuario);
            MDC.remove("error_code");
            MDC.remove("usuario");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("usuario", cleanedUsuario, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        String passwordEncriptada = usuario.getPassword();
        logger.info("passwordEncriptada",passwordEncriptada);
        String passwordDesencriptada = aesUtil.decrypt(passwordEncriptada);
        logger.info("passwordDesencriptada",passwordDesencriptada);

        // Verifica si está bloqueado
        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            CodigoError ce = CodigoError.USUARIO_BLOQUEADO;
            MDC.put("error_code", ce.getCodigo());
            logger.warn("Login bloqueado: usuario '{}' bloqueado hasta {}", cleanedUsuario, usuario.getBloqueadoHasta());
            MDC.remove("error_code");
            MDC.remove("usuario");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("usuario", cleanedUsuario, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        boolean passwordOk = passwordDesencriptada.equals(loginRequest.getContrasena());

        if (passwordOk) {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueadoHasta(null);
            usuarioRepository.save(usuario);

            logger.info("Login exitoso para usuario '{}'", cleanedUsuario);
            MDC.remove("usuario");

            String token = jwtUtil.generateToken(usuario.getUsuario(), usuario.getRol().name());
            LoginRsDTO dto = LoginRsDTO.builder()
                    .token(token)
                    .usuario(usuario.getUsuario())
                    .rol(usuario.getRol().name())
                    .build();
            return ResponseEntity.ok(dto);
        } else {
            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= MAX_INTENTOS) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(BLOQUEO_MINUTOS));
            }
            usuarioRepository.save(usuario);

            CodigoError ce = (intentos >= MAX_INTENTOS)
                    ? CodigoError.USUARIO_BLOQUEADO
                    : CodigoError.PASSWORD_INCORRECTA;

            MDC.put("error_code", ce.getCodigo());
            logger.warn("Login fallido: contraseña incorrecta para usuario '{}'. Intentos fallidos: {}{}",
                    cleanedUsuario, intentos,
                    intentos >= MAX_INTENTOS ?
                            " (usuario BLOQUEADO hasta " + usuario.getBloqueadoHasta() + ")" : "");

            MDC.remove("error_code");
            MDC.remove("usuario");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("usuario", cleanedUsuario, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }
    }
}
