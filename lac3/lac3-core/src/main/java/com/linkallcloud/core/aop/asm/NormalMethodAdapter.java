package com.linkallcloud.core.aop.asm;

import com.linkallcloud.core.repo.org.objectweb.asm.MethodVisitor;
import com.linkallcloud.core.repo.org.objectweb.asm.Opcodes;
import com.linkallcloud.core.repo.org.objectweb.asm.Type;

abstract class NormalMethodAdapter {

    final String desc;

    final int access;

    final MethodVisitor mv;

    /**
     * Argument types of the method visited by this adapter.
     */
    final Type[] argumentTypes;

    NormalMethodAdapter(MethodVisitor mv, String desc, int access) {
        this.mv = mv;
        this.desc = desc;
        this.access = access;
        argumentTypes = Type.getArgumentTypes(this.desc);
    }

    abstract void visitCode();

    /**
     * Generates the instructions to load all the method arguments on the stack.
     */
    void loadArgs() {
        loadArgs(0, argumentTypes.length);
    }

    void loadArgs(final int arg, final int count) {
        int index = 1;
        for (int i = 0; i < count; ++i) {
            Type t = argumentTypes[arg + i];
            loadInsn(t, index);
            index += t.getSize();
        }
    }

    /**
     * Generates the instruction to push a local variable on the stack.
     * 
     * @param type
     *            the type of the local variable to be loaded.
     * @param index
     *            an index in the frame's local variables array.
     */
    void loadInsn(final Type type, final int index) {
        mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), index);
    }

}
