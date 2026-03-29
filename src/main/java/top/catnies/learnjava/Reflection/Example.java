package top.catnies.learnjava.Reflection;

import net.momirealms.sparrow.reflection.clazz.SparrowClass;
import net.momirealms.sparrow.reflection.constructor.SConstructor1;
import net.momirealms.sparrow.reflection.constructor.SparrowConstructor;
import net.momirealms.sparrow.reflection.field.SField;
import net.momirealms.sparrow.reflection.field.SIntField;
import net.momirealms.sparrow.reflection.field.SparrowField;
import net.momirealms.sparrow.reflection.method.SMethod1;
import net.momirealms.sparrow.reflection.method.SparrowMethod;

// Import these static methods for conveniently using various Matchers
// Constructor matchers start with c, field matchers start with f, method matchers start with m, Type matchers start with t
import static net.momirealms.sparrow.reflection.constructor.matcher.ConstructorMatchers.*;
import static net.momirealms.sparrow.reflection.field.matcher.FieldMatchers.*;
import static net.momirealms.sparrow.reflection.method.matcher.MethodMatchers.*;

public final class Example {

    public static class TestClass {
        private String stringField;
        private int intField;
        private TestClass(String field) { this.stringField = field; }
        private String someMethod(String a) { return this.stringField + " " + a; }
    }

    public static void main(String[] args) {
        SparrowClass<TestClass> sClass = SparrowClass.of(TestClass.class);

        // Get any first private constructor
        SparrowConstructor<TestClass> constructor = sClass.getDeclaredSparrowConstructor(cAny(), 0);
        // Generate a constructor with one parameter
        // By the same token, asm$2 would be the constructor with 2 parameters.
        SConstructor1 asmConstructor = constructor.asm$1();
        TestClass testInstance = (TestClass) asmConstructor.newInstance("ByeBye");

        // Get the first field of type String
        SparrowField stringField = sClass.getDeclaredSparrowField(fType(String.class), 0);
        SField asmField = stringField.asm();
        asmField.set(testInstance, "Hello");

        // Reduce performance overhead from boxing and unboxing through the use of primitive type ASM implementations
        SparrowField intField = sClass.getDeclaredSparrowField(fType(int.class), 0);
        SIntField asmIntField = intField.asm$int();
        asmIntField.set(testInstance, 12345);

        // Get the method named 'someMethod' with the first parameter and return value both of type String
        SparrowMethod method = sClass.getDeclaredSparrowMethod(mNamed("someMethod").and(mTakeArgument(0, String.class).and(mReturnType(String.class))));
        SMethod1 asmMethod = method.asm$1();
        System.out.println(asmMethod.invoke(testInstance, "World"));
    }
}