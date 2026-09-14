package com.example.foro_backend.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateLabelHelper {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("es"));

    private DateLabelHelper() {
    }

    public static String formatCreatedLabel(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "recién publicado";
        }
        if (duration.toHours() < 1) {
            return "Publicado a las " + createdAt.format(TIME_FORMATTER);
        }
        if (duration.toHours() < 24) {
            return "Publicado a las " + createdAt.format(TIME_FORMATTER);
        }
        return "Publicado el " + createdAt.toLocalDate();
    }

    public static String formatRelativeLabel(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "recién publicado";
        }
        if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return "hace " + minutes + (minutes == 1 ? " minuto" : " minutos");
        }
        if (duration.toHours() < 24) {
            long hours = duration.toHours();
            return "hace " + hours + (hours == 1 ? " hora" : " horas");
        }
        long days = duration.toDays();
        return "hace " + days + (days == 1 ? " día" : " días");
    }
}
