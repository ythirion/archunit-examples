package rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.Function;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.*;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

// Check documentation here : https://www.archunit.org/userguide/html/000_Index.html
@AnalyzeClasses(packages = "rules", importOptions = ImportOption.DoNotIncludeTests.class)
public class Rules {
    @ArchTest
    public static final ArchRule cycles = ensureNoCycle("com.sample");

    @ArchTest
    public static final ArchRule layersShouldBeRespected = layeredArchitecture()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Model").definedBy("..model..")
            .layer("Repository").definedBy("..repository..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");

    @ArchTest
    public static final ArchRule repositories_reside_in_pkg_repository = classes().that()
            .areAnnotatedWith(Repository.class)
            .should().resideInAnyPackage("..repository..", "..repositories..")
            .andShould().beInterfaces();

    @ArchTest
    public static final ArchRule services_reside_in_pkg_service = classes().that()
            .areAnnotatedWith(Service.class)
            .should().resideInAnyPackage("..service.impl..", "..service..", "..services..")
            .andShould().notBeInterfaces();

    @ArchTest
    public static final ArchRule transactional_methods_are_declared_in_services = methods().that()
            .areAnnotatedWith(Transactional.class)
            .should().bePublic()
            .andShould().beDeclaredInClassesThat().areAnnotatedWith(Service.class);

    @ArchTest
    public static final ArchRule rest_controllers_resides_in_pkg_controller = classes().that()
            .areAnnotatedWith(RestController.class)
            .should().resideInAnyPackage("..controller..", "..controllers..");

    @ArchTest
    public static final ArchRule exceptions_resides_in_pkg_exception = classes().that()
            .areAssignableTo(Exception.class)
            .or().areAssignableTo(RuntimeException.class)
            .should().resideInAnyPackage("..exception..", "..exceptions..");

    @ArchTest
    public static final ArchRule mapstruct_mappers_resides_in_pkg_mapper = classes().that()
            .areAnnotatedWith(Mapper.class)
            .should().resideInAnyPackage("..mapper..", "..mappers..");

    public static ArchRule ensureNoCycle(String projectPackage) {
        return slices().matching(projectPackage + ".(*)..").should()
                .beFreeOfCycles();
    }

    @ArchTest
    public static final ArchRule interfaces_should_not_have_name_starting_with_i_in_Java =
            noClasses().that().areInterfaces().should().haveNameMatching("^.*[.]I[A-Z].*")
                    .because("This is not C# you're doing here :-)");

    private static class AnyClassPredicate extends DescribedPredicate<JavaClass> {
        AnyClassPredicate() {
            super("any class");
        }

        @Override
        public boolean apply(JavaClass input) {
            return true;
        }
    }

    private static class MapperPredicate extends DescribedPredicate<JavaClass> {
        MapperPredicate() {
            super("mapper class");
        }

        @Override
        public boolean apply(JavaClass input) {
            return input.isAnnotatedWith(Mapper.class);
        }
    }

    @ArchTest
    public static final ArchRule implementation_should_not_have_name_ending_with_Impl =
            noClasses().that()
                    .areTopLevelClasses()
                    .and().implement(new AnyClassPredicate())
                    .and().doNotImplement(new MapperPredicate()) // Generated stuff, like MapStruct
                    .should().haveNameMatching("^.*Impl$")
                    .because("Having an implementation with name ending with `Impl` is discouraged.\n" +
                            "If you have an interface and only one implementation, just get rid of the implementation already, " +
                            "otherwise please rename it to something more useful\n" +
                            "ex: CardsService -> RestCardsService, JmsCardsService, ...");

    @ArchTest
    public static final ArchRule interfaces_should_not_have_name_ending_with_interface =
            noClasses().that().areInterfaces().should().haveNameMatching(".*Interface");

    @ArchTest
    public static final ArchRule no_access_to_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    public static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    public static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    public static final ArchRule loggers_should_be_private_static_final =
            fields().that().haveRawType(Logger.class)
                    .should().bePrivate()
                    .andShould().beStatic()
                    .andShould().beFinal()
                    .because("we agreed on this convention");

    @ArchTest
    public static final ArchRule rest_mapping_methods_must_be_kebab_case =
            methods().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().notBeAnnotatedWith(nonCompliantKebabCaseMapping());

    @ArchTest
    public static final ArchRule rest_mapping_classes_must_be_kebab_case =
            classes().that().areAnnotatedWith(RequestMapping.class)
                    .should().notBeAnnotatedWith(nonCompliantKebabCaseMapping());

    public static DescribedPredicate<JavaAnnotation<?>> nonCompliantKebabCaseMapping() {
        return nonCompliantMapping("kebab-case",
                path -> Arrays.stream(path.split("/")).anyMatch(s -> !s.matches("^[a-z0-9-]*$")));
    }

    @ArchTest
    public static final ArchRule rest_mapping_methods_must_have_version_correctly_located =
            methods().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().notBeAnnotatedWith(nonCompliantVersionMapping());

    @ArchTest
    public static final ArchRule rest_mapping_classes_must_have_version_correctly_located =
            classes().that().areAnnotatedWith(RequestMapping.class)
                    .should().notBeAnnotatedWith(nonCompliantVersionMapping());

    public static DescribedPredicate<JavaAnnotation<?>> nonCompliantVersionMapping() {
        return nonCompliantMapping("versioning",
                path -> path.matches("^/.*/v\\d(.\\d)?.*"));
    }

    @ArchTest
    public static final ArchRule rest_mapping_methods_must_not_contain_file_extension =
            methods().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().notBeAnnotatedWith(nonCompliantFileExtensionMapping());

    @ArchTest
    public static final ArchRule rest_mapping_classes_must_not_contain_file_extension =
            classes().that().areAnnotatedWith(RequestMapping.class)
                    .should().notBeAnnotatedWith(nonCompliantFileExtensionMapping());

    public static DescribedPredicate<JavaAnnotation<?>> nonCompliantFileExtensionMapping() {
        return nonCompliantMapping("file extension",
                path -> Arrays.stream(path.split("/")).anyMatch(s -> s.matches("^.*\\.[a-zA-Z0-9]+$")));
    }

    public static DescribedPredicate<JavaAnnotation<?>> nonCompliantMapping(String what,
                                                                            Function<String, Boolean> app) {
        return new DescribedPredicate<>(
                "a Spring route mapping which is not compliant to our REST guidelines regarding " + what) {
            @Override
            public boolean apply(JavaAnnotation<?> javaAnnotation) {
                JavaClass rawType = javaAnnotation.getRawType();
                if (!(rawType.isAssignableFrom(RequestMapping.class)
                        || rawType.isAssignableFrom(PostMapping.class)
                        || rawType.isAssignableFrom(DeleteMapping.class)
                        || rawType.isAssignableFrom(PutMapping.class)
                        || rawType.isAssignableFrom(GetMapping.class)
                        || rawType.isAssignableFrom(PatchMapping.class))) {
                    return false;
                }
                String path = ((String[]) javaAnnotation.get("value").or(new String[]{"/empty"}))[0]
                        .replaceAll("\\{[^}]+}", "param");
                return app.apply(path);
            }
        };
    }

    @ArchTest
    public static final ArchRule no_joda_time = NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    public static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    public static final ArchRule no_classes_should_access_standard_streams_or_throw_generic_exceptions =
            CompositeArchRule.of(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS)
                    .and(NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS);

    public static ArchCondition<JavaMethod> notBeVoid() {
        return new ArchCondition<JavaMethod>("not return void") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean matches = !"void".equals(method.getRawReturnType().getName());
                String message = method.getFullName() + " returns " + method.getRawReturnType().getName();
                events.add(new SimpleConditionEvent(method, matches, message));
            }
        };
    }

    @ArchTest
    public static final ArchRule no_getter_can_return_void = methods().that().haveNameMatching("get.*")
            .should(notBeVoid())
            .because("any method which gets something should actually return something");

    @ArchTest
    public static final ArchRule iser_haser_must_return_boolean =
            methods().that().haveNameMatching("is[A-Z].*")
                    .or().haveNameMatching("has[A-Z].*")
                    .should().haveRawReturnType(Boolean.class)
                    .orShould().haveRawReturnType(boolean.class)
                    .because("any method which fetch a state should actually return something (a boolean)");
}
