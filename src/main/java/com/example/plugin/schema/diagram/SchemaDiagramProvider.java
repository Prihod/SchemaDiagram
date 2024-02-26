package com.example.plugin.schema.diagram;

import com.intellij.diagram.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchemaDiagramProvider extends DiagramProvider<PsiElement> {
    public static final String ID = "SchemaDiagram";
    private static final Logger LOG = Logger.getInstance(SchemaDiagramProvider.class);

    @Override
    public @NotNull String getID() {
        return ID;
    }

    @Override
    public @NotNull @NlsContexts.Label String getPresentableName() {
        return "Schema Diagram";
    }

    @Override
    public @NotNull DiagramDataModel<PsiElement> createDataModel(@NotNull Project project, @Nullable PsiElement element, @Nullable VirtualFile virtualFile, @NotNull DiagramPresentationModel diagramPresentationModel) {
        return new SchemaDiagramModel(project, (XmlFile) element, this);
    }

    @Override
    public @NotNull DiagramVisibilityManager createVisibilityManager() {
        return new SchemaDiagramVisibilityManager();
    }

    @Override
    public @NotNull DiagramElementManager<PsiElement> getElementManager() {
        DiagramElementManager<PsiElement> elementManager = new SchemaDiagramElementManager();
        elementManager.setUmlProvider(this);
        return elementManager;

    }

    @Override
    public @NotNull DiagramVfsResolver<PsiElement> getVfsResolver() {
        return new SchemaDiagramVfsResolver();
    }

    @Override
    public @NotNull DiagramNodeContentManager createNodeContentManager() {
        return new SchemaNodeContentManager();
    }

    @Override
    public @NotNull DiagramRelationshipManager<PsiElement> getRelationshipManager() {
        return null;
    }

    public static SchemaDiagramProvider getInstance() {
        DiagramProvider<PsiElement> provider = DiagramProvider.findByID(ID);
        return (SchemaDiagramProvider) provider;
    }
}
