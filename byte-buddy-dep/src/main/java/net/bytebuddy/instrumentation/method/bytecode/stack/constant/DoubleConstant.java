package net.bytebuddy.instrumentation.method.bytecode.stack.constant;

import net.bytebuddy.instrumentation.Instrumentation;
import net.bytebuddy.instrumentation.method.bytecode.stack.StackManipulation;
import net.bytebuddy.instrumentation.method.bytecode.stack.StackSize;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * This class is responsible for loading any {@code double} constant onto the operand stack.
 */
public enum DoubleConstant implements StackManipulation {

    /**
     * A {@code double} constant of value {@code 0.0}.
     */
    ZERO(Opcodes.DCONST_0),

    /**
     * A {@code double} constant of value {@code 1.0}.
     */
    ONE(Opcodes.DCONST_1);

    /**
     * The size impact of loading a {@code double} constant onto the operand stack.
     */
    private static final StackManipulation.Size SIZE = StackSize.DOUBLE.toIncreasingSize();

    /**
     * The shortcut opcode for loading a {@code double} constant.
     */
    private final int opcode;

    /**
     * Creates a new shortcut operation for loading a common {@code double} onto the operand stack.
     *
     * @param opcode The shortcut opcode for loading a {@code double} constant.
     */
    private DoubleConstant(int opcode) {
        this.opcode = opcode;
    }

    /**
     * Creates a stack manipulation for loading a {@code double} value onto the operand stack.
     * <p>&nbsp;</p>
     * This is achieved either by invoking a specific opcode, if any, or by creating a constant pool entry.
     *
     * @param value The {@code double} value to load onto the stack.
     * @return A stack manipulation for loading the given {@code double} value.
     */
    public static StackManipulation forValue(double value) {
        if (value == 0d) {
            return ZERO;
        } else if (value == 1d) {
            return ONE;
        } else {
            return new ConstantPool(value);
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public StackManipulation.Size apply(MethodVisitor methodVisitor, Instrumentation.Context instrumentationContext) {
        methodVisitor.visitInsn(opcode);
        return SIZE;
    }

    /**
     * A stack manipulation for loading a {@code double} value from a class's constant pool onto the operand stack.
     */
    private static class ConstantPool implements StackManipulation {

        /**
         * The {@code double} value to be loaded onto the operand stack.
         */
        private final double value;

        /**
         * Creates a new constant pool load operation.
         *
         * @param value The {@code double} value to be loaded onto the operand stack.
         */
        private ConstantPool(double value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public Size apply(MethodVisitor methodVisitor, Instrumentation.Context instrumentationContext) {
            methodVisitor.visitLdcInsn(value);
            return SIZE;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || !(other == null || getClass() != other.getClass())
                    && Double.compare(((ConstantPool) other).value, value) == 0;
        }

        @Override
        public int hashCode() {
            long temp = Double.doubleToLongBits(value);
            return (int) (temp ^ (temp >>> 32));
        }

        @Override
        public String toString() {
            return "DoubleConstant.ConstantPool{value=" + value + '}';
        }
    }
}
