package com.phoenix.amazonbackend.audit;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @return - String
     */
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of("");
    }
}
