package it.iacovelli.grocerybe.config;

import org.hibernate.dialect.PostgreSQLPGObjectJdbcType;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(NativeImageRuntimeHintsConfiguration.HibernateRegistrar.class)
public class NativeImageRuntimeHintsConfiguration {

    static class HibernateRegistrar implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
            try {
                // Temporary hint, should be included into the official spring boot project
                hints.reflection().registerTypeIfPresent(classLoader, "org.postgresql.util.PGobject",
                        (hint) -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INTROSPECT_PUBLIC_METHODS)
                                .onReachableType(PostgreSQLPGObjectJdbcType.class));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
