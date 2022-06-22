package com;

import com.intellij.psi.*;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestDemo  extends LightJavaCodeInsightFixtureTestCase {

    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return JAVA_8;
    }

    @Test
    public void testFun() {
        String testSrcHome = System.getProperty("project.home") + "\\src\\test\\java\\com\\";

        String HelloContent = readString(testSrcHome+"Hello.java");
        String HelloWorldContent = readString(testSrcHome+"HelloWorld.java");
        String Invoker1Content = readString(testSrcHome+"Invoker1.java");
        String Invoker2Content = readString(testSrcHome+"Invoker2.java");

        this.myFixture.addFileToProject("Hello.java",HelloContent);
        this.myFixture.addFileToProject("HelloWorld.java",HelloWorldContent);

        PsiFile invoker1PsiFile = this.myFixture.addFileToProject("Invoker1.java",Invoker1Content);
        invoker1PsiFile.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiElement element = expression.getMethodExpression().resolve();
                System.out.println(expression.getText() + " resolve: element is null ? ["+ (element == null)+"]");
                super.visitMethodCallExpression(expression);
            }
        });

        PsiFile invoker2PsiFile = this.myFixture.addFileToProject("Invoker2.java",Invoker2Content);
        invoker2PsiFile.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiElement element = expression.getMethodExpression().resolve();
                System.out.println(expression.getText() + " resolve: element is null ? ["+ (element == null) +"]");
                super.visitMethodCallExpression(expression);
            }
        });
    }

    static String readString(String path) {
        try {
           return Files.readString(Path.of(path));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
