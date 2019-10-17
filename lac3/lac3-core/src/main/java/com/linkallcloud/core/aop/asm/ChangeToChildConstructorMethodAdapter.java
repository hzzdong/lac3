package com.linkallcloud.core.aop.asm;

import static com.linkallcloud.core.repo.org.objectweb.asm.Opcodes.ALOAD;
import static com.linkallcloud.core.repo.org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static com.linkallcloud.core.repo.org.objectweb.asm.Opcodes.RETURN;

import com.linkallcloud.core.repo.org.objectweb.asm.Label;
import com.linkallcloud.core.repo.org.objectweb.asm.MethodVisitor;

class ChangeToChildConstructorMethodAdapter extends NormalMethodAdapter {

    private String superClassName;

    ChangeToChildConstructorMethodAdapter(    MethodVisitor mv,
                                                    String desc,
                                                    int access,
                                                    String superClassName) {
        super(mv, desc, access);
        this.superClassName = superClassName;
    }

    void visitCode() {
        mv.visitCode();
        // start of fuck linenumber
        Label tmp = new Label();
        mv.visitLabel(tmp);
        mv.visitLineNumber(1, tmp);
        // end of Linenumber
        mv.visitVarInsn(ALOAD, 0);
        loadArgs();
        mv.visitMethodInsn(INVOKESPECIAL, superClassName, "<init>", desc, false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }
}