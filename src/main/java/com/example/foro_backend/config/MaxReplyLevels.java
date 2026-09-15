package com.example.foro_backend.config;

public final class MaxReplyLevels {

    private final Integer limit;

    private MaxReplyLevels(Integer limit) {
        this.limit = limit;
    }

    public static MaxReplyLevels parse(String value) {
        if (value == null || value.isBlank()
                || "ilimitado".equalsIgnoreCase(value.trim())
                || "unlimited".equalsIgnoreCase(value.trim())
                || "x".equalsIgnoreCase(value.trim())) {
            return new MaxReplyLevels(null);
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed != 3 && parsed != 5) {
                throw new IllegalArgumentException(
                        "app.forum.max-reply-levels solo admite los valores 3, 5 o ilimitado"
                );
            }
            return new MaxReplyLevels(parsed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "app.forum.max-reply-levels solo admite los valores 3, 5 o ilimitado"
            );
        }
    }

    public boolean isUnlimited() {
        return limit == null;
    }

    public Integer limit() {
        return limit;
    }

    public boolean allowsLevel(int level) {
        return limit == null || level <= limit;
    }
}
