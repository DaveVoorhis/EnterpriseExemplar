package org.reldb.exemplars.java.backend.testing.architecture;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.util.List;
import org.junit.jupiter.api.Test;

class TestLayeredArchitectureEnforcement {

    static final String PACKAGE = "org.reldb.exemplars.java.backend";

    @Test
    void givenApplicationClassesThenNoLayerViolationsShouldExist() {

        final var packagePrefix = PACKAGE + ".";
        final var classes = new ClassFileImporter()
                .withImportOptions(List.of(DO_NOT_INCLUDE_TESTS, DO_NOT_INCLUDE_PACKAGE_INFOS))
                .importPackages(packagePrefix);

        layeredArchitecture().consideringOnlyDependenciesInLayers()

                .layer("Api").definedBy(packagePrefix + "api..")
                .layer("ApiService").definedBy(packagePrefix + "api.service..")
                .layer("ApiMappers").definedBy(packagePrefix + "api.mappers..")
                .layer("ApiModel").definedBy(packagePrefix + "api.model..")
                .layer("ApiInterceptors").definedBy(packagePrefix + "api.interceptors..")
                .layer("Exception").definedBy(packagePrefix + "exception..")
                .layer("Enums").definedBy(packagePrefix + "enums..")
                .layer("Model").definedBy(packagePrefix + "model..")
                .layer("Persistence").definedBy(packagePrefix + "persistence..")
                .layer("Service").definedBy(packagePrefix + "service..")
                .layer("Cron").definedBy(packagePrefix + "cron..")

                .whereLayer("Api").mayNotBeAccessedByAnyLayer()
                .whereLayer("ApiService").mayOnlyAccessLayers("Service", "ApiMappers", "ApiModel", "Model", "Enums")
                .whereLayer("Cron").mayOnlyAccessLayers("Service")
                .whereLayer("Enums").mayNotAccessAnyLayer()
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")

                .ensureAllClassesAreContainedInArchitectureIgnoring(
                        HasName.Predicates.nameStartingWith(packagePrefix + "Application"))

                .check(classes);
    }
}
