package demo.delivery;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import io.github.lambig.ab.architecture.MethodAccessRules;
import org.junit.Test;

public final class BoundaryCheck {
    @Test
    public void restrictsTransitionsToOperations() {
        MethodAccessRules.annotatedMethodsOnlyAccessedByAnnotatedClasses(Transition.class, Operation.class)
                .check(new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages("demo.delivery"));
        System.out.println("H1 boundary passed");
    }
}
