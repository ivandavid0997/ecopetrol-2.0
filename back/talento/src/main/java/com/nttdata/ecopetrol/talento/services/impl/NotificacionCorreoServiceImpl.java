package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.services.NotificacionCorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionCorreoServiceImpl implements NotificacionCorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String texto) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(texto);
        mensaje.setFrom("ivandavid0997@gmail.com");
        try {
            mailSender.send(mensaje);
        } catch (MailException ex) {
            System.err.println("No se pudo enviar la notificacion por correo: " + ex.getMessage());
        }
    }
}
