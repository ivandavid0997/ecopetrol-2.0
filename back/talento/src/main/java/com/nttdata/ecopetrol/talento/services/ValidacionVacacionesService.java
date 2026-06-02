package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.model.Calamidad;
import com.nttdata.ecopetrol.talento.model.DiaCumpleanio;
import com.nttdata.ecopetrol.talento.model.Incapacidad;
import com.nttdata.ecopetrol.talento.model.Vacaciones;

public interface ValidacionVacacionesService {

    boolean validarDiasVacacionesDisponibles(Vacaciones vacaciones) throws InterruptedException;

    boolean validarDiasIncapacidadDisponibles(Incapacidad incapacidad) throws InterruptedException;

    boolean validarDiasCalamidadDisponibles(Calamidad calamidad) throws InterruptedException;

    boolean validarDiasCumpleanioDisponibles(DiaCumpleanio diaCumpleanio) throws InterruptedException;
}
