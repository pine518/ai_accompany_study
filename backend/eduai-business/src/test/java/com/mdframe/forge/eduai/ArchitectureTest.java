package com.mdframe.forge.eduai;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.mdframe.forge.eduai",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class ArchitectureTest {

    @ArchTest
    static final ArchRule BUSINESS_MUST_NOT_DEPEND_ON_FORGE_PLUGIN_IMPLEMENTATIONS = noClasses()
            .that().resideInAPackage("com.mdframe.forge.eduai..")
            .should().dependOnClassesThat().resideInAPackage("com.mdframe.forge.plugin..");
}
