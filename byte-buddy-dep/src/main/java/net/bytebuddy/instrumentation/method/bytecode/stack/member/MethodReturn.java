package net.bytebuddy.instrumentation.method.bytecode.stack.member;

import net.bytebuddy.instrumentation.Instrumentation;
import net.bytebuddy.instrumentation.method.bytecode.stack.StackManipulation;
import net.bytebuddy.instrumentation.method.bytecode.stack.StackSize;
import net.bytebuddy.instrumentation.type.TypeDescription;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * A stack manipulation returning a value of a given type.
 */
public enum MethodReturn implements StackManipulation {

    /**
     * The method return handler for returning a JVM-integer.
     */
    INTEGER(Opcodes.IRETURN, StackSize.SINGLE),

    /**
     * The method return handler for returning a {@code double}.
     */
    DOUBLE(Opcodes.DRETURN, StackSize.DOUBLE),

    /**
     * The method return handler for returning a {@code float}.
     */
    FLOAT(Opcodes.FRETURN, StackSize.SINGLE),

    /**
     * The method return handler for returning a {@code long}.
     */
    LONG(Opcodes.LRETURN, StackSize.DOUBLE),

    /**
     * The method return handler for returning {@code void}.
     */
    VOID(Opcodes.RETURN, StackSize.ZERO),

    /**
     * The method return handler for returning a reference type.
     */
    ANY_REFERENCE(Opcodes.ARETURN, StackSize.SINGLE);

    /**
     * The opcode of this operation.
     */
    private final int returnOpcode;

    /**
     * The operand stack size change that is implied by this operation.
     */
    private final Size size;

    /**
     * Creates a new method return manipulation.
     *
     * @param returnOpcode The opcode of this operation.
     * @param stackSize    The operand stack size change that is implied by this operation.
     */
    private MethodReturn(int returnOpcode, StackSize stackSize) {
        this.returnOpcode = returnOpcode;
        size = stackSize.toDecreasingSize();
    }

    /**
     * Returns a method return corresponding to a given type.
     *
     * @param typeDescription The type to be returned.
     * @return The stack manipulation representing the method return.
     */
    public static StackManipulation returning(TypeDescription typeDescription) {
        if (typeDescription.isPrimitive()) {
            if (typeDescription.represents(long.class)) {
                return LONG;
            } else if (typeDescription.represents(double.class)) {
                return DOUBLE;
            } else if (typeDescription.represents(float.class)) {
                return FLOAT;
            } else if (typeDescription.represents(void.class)) {
                return VOID;
            } else {
                return INTEGER;
            }
        } else {
            return ANY_REFERENCE;
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Instrumentation.Context instrumentationContext) {
        methodVisitor.visitInsn(returnOpcode);
        return size;
    }
}
