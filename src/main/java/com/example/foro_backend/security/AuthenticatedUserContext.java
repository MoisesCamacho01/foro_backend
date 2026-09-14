package com.example.foro_backend.security;

public final class AuthenticatedUserContext {

    private static final ThreadLocal<String> CURRENT_ALIAS = new ThreadLocal<>();

    private AuthenticatedUserContext() {
    }

    public static void setAlias(String alias) {
        CURRENT_ALIAS.set(alias);
    }

    public static String getAlias() {
        return CURRENT_ALIAS.get();
    }

    public static void clear() {
        CURRENT_ALIAS.remove();
    }
}
