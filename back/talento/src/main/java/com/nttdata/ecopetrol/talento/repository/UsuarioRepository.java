package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByRol(String rol); // Buscar usuarios por rol, por ejemplo "LIDER" o "EMPLEADO"
    Usuario findByUsuario(String usuario);
}
