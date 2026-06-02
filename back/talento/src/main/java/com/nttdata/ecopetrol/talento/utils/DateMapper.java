package com.nttdata.ecopetrol.talento.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/** Conversión entre java.util.Date (DTOs) y java.time.LocalDate (entidades). */
public final class DateMapper {

    private DateMapper() {}

    public static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate date) {
        return date == null ? null : Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
