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

    public LoginServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<?> login(LoginRqDTO loginRequest) throws Exception {
        if (loginRequest == null || loginRequest.getUsuario() == null) {
            MDC.put("codigo_error", CodigoError.LOGIN_FALLIDO.getCodigo());
            logger.warn("Login fallido: request o usuario nulo");
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Usuario obligatorio"));
        }

        String cleanedUsuario = loginRequest.getUsuario().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByUsuario(cleanedUsuario);

        if (usuario == null) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.warn("Login fallido: usuario '{}' no encontrado", cleanedUsuario);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }

        String passwordEncriptada = usuario.getPassword();
        String passwordDesencriptada = AesUtil.decrypt(passwordEncriptada);

        // Verifica si está bloqueado
        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            MDC.put("codigo_error", CodigoError.USUARIO_BLOQUEADO.getCodigo());
            logger.warn("Login bloqueado: usuario '{}' bloqueado hasta {}", cleanedUsuario, usuario.getBloqueadoHasta());
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario bloqueado hasta " + usuario.getBloqueadoHasta()));
        }

        boolean passwordOk = passwordDesencriptada.equals(loginRequest.getContrasena());

        if (passwordOk) {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueadoHasta(null);
            usuarioRepository.save(usuario);

            logger.info("Login exitoso para usuario '{}'", cleanedUsuario);

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

            MDC.put("codigo_error", CodigoError.PASSWORD_INCORRECTA.getCodigo());
            logger.warn("Login fallido: contraseña incorrecta para usuario '{}'. Intentos fallidos: {}{}",
                    cleanedUsuario, intentos,
                    intentos >= MAX_INTENTOS ?
                            " (usuario BLOQUEADO hasta " + usuario.getBloqueadoHasta() + ")" : "");

            MDC.remove("codigo_error");

            String msg = (intentos >= MAX_INTENTOS)
                    ? "Usuario bloqueado por superar intentos fallidos. Intente más tarde."
                    : "Usuario o contraseña incorrectos";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", msg));
        }
    }
}
