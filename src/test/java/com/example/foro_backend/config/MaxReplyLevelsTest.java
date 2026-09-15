package com.example.foro_backend.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaxReplyLevelsTest {

    @Test
    void parseAcceptsThreeAndFive() {
        assertThat(MaxReplyLevels.parse("3").limit()).isEqualTo(3);
        assertThat(MaxReplyLevels.parse("5").limit()).isEqualTo(5);
    }

    @Test
    void parseAcceptsUnlimitedValues() {
        assertThat(MaxReplyLevels.parse("ilimitado").isUnlimited()).isTrue();
        assertThat(MaxReplyLevels.parse("unlimited").isUnlimited()).isTrue();
        assertThat(MaxReplyLevels.parse("x").isUnlimited()).isTrue();
        assertThat(MaxReplyLevels.parse(null).isUnlimited()).isTrue();
    }

    @Test
    void parseRejectsInvalidValues() {
        assertThatThrownBy(() -> MaxReplyLevels.parse("4"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> MaxReplyLevels.parse("abc"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void allowsLevelRespectsLimit() {
        MaxReplyLevels limited = MaxReplyLevels.parse("3");
        assertThat(limited.allowsLevel(1)).isTrue();
        assertThat(limited.allowsLevel(3)).isTrue();
        assertThat(limited.allowsLevel(4)).isFalse();
    }

    @Test
    void allowsLevelWhenUnlimited() {
        MaxReplyLevels unlimited = MaxReplyLevels.parse("ilimitado");
        assertThat(unlimited.allowsLevel(10)).isTrue();
    }
}
