package com.example.plugin.schema.diagram;

import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class SchemaDiagramVfsResolver implements DiagramVfsResolver<PsiElement> {
    private static final Logger LOG = Logger.getInstance(SchemaDiagramVfsResolver.class);

    @Override
    public String getQualifiedName(PsiElement element) {
        if (element == null || !element.isValid()) {
            return null;
        }
        if (SchemaDiagramUtil.isXmlSchemaFile(element)) {
            return ((XmlFile) element).getVirtualFile().getCanonicalPath();
        }
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolveElementByFQN(String s, Project project) {
        return resolveAsFile(s, project);
    }

    private @Nullable PsiElement resolveAsFile(@NotNull String path, @NotNull Project project) {
        VirtualFile file = VirtualFileManager.getInstance().findFileByNioPath(Path.of(path));
        if (file == null) {
            return null;
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (!(psiFile instanceof XmlFile)) {
            return null;
        }

        return psiFile;
    }

}
