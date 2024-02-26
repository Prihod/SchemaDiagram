package com.example.plugin.schema.diagram;

import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramEdge;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SchemaDiagramModel extends DiagramDataModel<PsiElement> {
    private static final Logger LOG = Logger.getInstance(SchemaDiagramModel.class);
    private final List<SchemaNode> nodes = new ArrayList<>();
    private final List<SchemaEdge> edges = new ArrayList<>();
    private final Map<PsiElement, SchemaNode> nodeMap = new HashMap<>();
    private final XmlFile file;

    public SchemaDiagramModel(@NotNull Project project, @Nullable XmlFile file, @NotNull DiagramProvider<PsiElement> provider) {
        super(project, provider);
        this.file = file;
    }

    @Override
    public @NotNull ModificationTracker getModificationTracker() {
        return this;
    }

    @Override
    public @NotNull Collection<? extends DiagramNode<PsiElement>> getNodes() {
        return nodes;
    }

    @Override
    public @NotNull String getNodeName(@NotNull DiagramNode<PsiElement> node) {
        final PsiElement element = node.getIdentifyingElement();
        return element.getText();
    }

    @Override
    public @Nullable DiagramNode<PsiElement> addElement(@Nullable PsiElement psiElement) {
        return null;
    }

    @Override
    public @NotNull Collection<? extends DiagramEdge<PsiElement>> getEdges() {
        return edges;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void refreshDataModel() {
        LOG.warn("refreshDataModel");
        nodes.clear();
        edges.clear();
        nodeMap.clear();
        String packageName = detectPackageName();
        XmlTag[] tables = PsiTreeUtil.findChildrenOfType(file, XmlTag.class)
                .stream()
                .filter(tag -> tag.getName().equals("object"))
                .toArray(XmlTag[]::new);

        for (XmlTag table : tables) {
            createSchemaNode(table, packageName);
        }
    }

    private @Nullable SchemaNode createSchemaNode(@NotNull XmlTag table, @NotNull String packageName) {
        SchemaNode node = new SchemaNode(table, packageName, getProvider());
        nodeMap.put(table, node);
        nodes.add(node);
        return node;
    }

    private @Nullable String detectPackageName() {
        XmlTag model = file.getRootTag();
        if (model != null) {
            XmlAttribute attr = model.getAttribute("package");
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;

    }

}
