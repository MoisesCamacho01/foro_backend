package com.example.foro_backend.util;

import com.example.foro_backend.dto.forum.ForumUserResponse;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class UserPresentationHelper {

    private record SeedAuthor(String displayName, String handle, String initials) {
    }

    private static final Map<String, SeedAuthor> SEED_AUTHORS = Map.of(
            "carlos_dev", new SeedAuthor("Carlos Rodríguez", "@carlos_dev", "CR"),
            "mariana_lopez", new SeedAuthor("Mariana López", null, "ML"),
            "pedro_sanchez", new SeedAuthor("Pedro Sánchez", "@pedro_sanchez", "PS"),
            "ana_garcia", new SeedAuthor("Ana García", "@ana_garcia", "AG")
    );

    private UserPresentationHelper() {
    }

    public static ForumUserResponse buildForumUser(String authorAlias, int level, String currentUserAlias) {
        boolean isCurrentUser = authorAlias.equalsIgnoreCase(currentUserAlias);
        if (isCurrentUser) {
            return buildFromAlias(authorAlias, "current");
        }

        Optional<SeedAuthor> seedAuthor = Optional.ofNullable(SEED_AUTHORS.get(authorAlias.toLowerCase(Locale.ROOT)));
        if (seedAuthor.isPresent()) {
            SeedAuthor seed = seedAuthor.get();
            String tone = level == 0 ? "author" : "level" + Math.min(level, 3);
            return new ForumUserResponse(seed.displayName(), seed.handle(), seed.initials(), tone);
        }

        return buildFromAlias(authorAlias, toneForLevel(level));
    }

    private static ForumUserResponse buildFromAlias(String alias, String tone) {
        String normalized = alias.trim();
        return new ForumUserResponse(
                normalized,
                "@" + normalized,
                initialsFromAlias(normalized),
                tone
        );
    }

    private static String toneForLevel(int level) {
        if (level <= 0) {
            return "author";
        }
        return "level" + Math.min(level, 3);
    }

    private static String initialsFromAlias(String alias) {
        String[] parts = alias.split("[^a-zA-Z0-9]+");
        if (parts.length >= 2 && !parts[0].isBlank() && !parts[1].isBlank()) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase(Locale.ROOT);
        }
        if (alias.length() >= 2) {
            return alias.substring(0, 2).toUpperCase(Locale.ROOT);
        }
        return alias.toUpperCase(Locale.ROOT);
    }
}
