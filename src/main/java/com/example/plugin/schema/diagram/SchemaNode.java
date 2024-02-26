package com.example.plugin.schema.diagram;

import com.example.plugin.schema.icons.SchemaIcons;
import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramProvider;
import com.intellij.diagram.PsiDiagramNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.SimpleColoredText;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.diagram.DiagramElementManager.DEFAULT_TITLE_ATTR;

public class SchemaNode extends PsiDiagramNode<PsiElement> {
    private static final Logger LOG = Logger.getInstance(SchemaNode.class);
    private final String packageName;

    public SchemaNode(@NotNull PsiElement psiElement, String packageName, @NotNull DiagramProvider<PsiElement> provider) {
        super(psiElement, provider);
        this.packageName = packageName;
    }

    @Override
    public @Nullable Icon getIcon() {
        return SchemaIcons.TABLE;
    }

    @Override
    public @Nullable SimpleColoredText getPresentableTitle() {
        return new SimpleColoredText(getName(), DEFAULT_TITLE_ATTR);
    }

    @Override
    public @Nullable @Nls String getTooltip() {
        return getName();
    }

    @Override
    public @NotNull PsiElement getIdentifyingElement() {
        return ApplicationManager.getApplication().runReadAction((Computable<PsiElement>) () -> {
            return super.getIdentifyingElement();
        });

    }

    @NotNull
    public String getName() {
        return ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
            return SchemaDiagramUtil.getTableName(getElement());
        });
    }

    public String getPackageName() {
        return packageName;
    }

    @NotNull
    public XmlTag getTag() {
        return ((XmlTag) getElement());
    }

}
