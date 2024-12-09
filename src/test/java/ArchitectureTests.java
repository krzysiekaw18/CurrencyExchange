import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

@AnalyzeClasses(packages = {"com.ks.currencyexchange.adapter", "com.ks.currencyexchange.application"},
                importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
class ArchitectureTests {

    @ArchTest
    @ArchIgnore
    static final ArchRule onion_architecture_is_respected = onionArchitecture()
            .domainModels("..application.domain.model..")
            .domainServices("..application.port..")
            .applicationServices("..application.usecase..")
            .adapter("in.api.account", "..adapter.in.api.account..")
            .adapter("in.api.user", "..adapter.in.api.user..")
            .adapter("out.api.nbp", "..adapter.out.api.nbp..")
            .adapter("out.persistence.account", "..adapter.out.persistence.account..")
            .adapter("out.persistence.user", "..adapter.out.persistence.user..");

    @ArchTest
    @ArchIgnore
    static ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
}
