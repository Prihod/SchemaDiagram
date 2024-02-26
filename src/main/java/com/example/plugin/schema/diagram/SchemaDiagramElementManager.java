package com.example.plugin.schema.diagram;

import com.intellij.diagram.AbstractDiagramElementManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchemaDiagramElementManager extends AbstractDiagramElementManager<PsiElement> {
    @Override
    public @Nullable PsiElement findInDataContext(@NotNull DataContext context) {
        PsiFile file = CommonDataKeys.PSI_FILE.getData(context);
        if (!(file instanceof XmlFile xmlFile)) return null;
        if (isAcceptableAsSource(xmlFile)) {
            return xmlFile;
        }
        return null;
    }

    @Override
    public boolean isAcceptableAsNode(@Nullable Object o) {
        return true;
    }

    @Override
    public @Nullable @Nls String getElementTitle(PsiElement element) {
        if (element instanceof XmlFile) {
            VirtualFile virtualFile = ((XmlFile) element).getVirtualFile();
            if (virtualFile != null) {
                return virtualFile.getPresentableName();
            }
        } else if (element instanceof XmlTag xmlTag) {
            return getElementTitle(xmlTag);
        }
        return null;
    }

    @Override
    public @Nullable @Nls String getNodeTooltip(PsiElement psiElement) {
        return null;
    }

    public static boolean isAcceptableAsSource(@Nullable Object object) {
        return true;// object instanceof XmlFile;
    }

    @Nullable
    private String getElementTitle(@Nullable XmlTag tag) {
        String name = null;
        if (tag != null && tag.getParent() != null) {
            switch (tag.getName()) {
                case "object":
                    name = StringUtil.notNullize(tag.getAttributeValue("table"));
                    break;
                case "field":
                    name = StringUtil.notNullize(tag.getAttributeValue("key"));
                    break;
            }
        }
        return name;
    }
}
