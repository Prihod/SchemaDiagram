package com.example.plugin.schema.diagram;

import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class SchemaEdge extends DiagramEdgeBase<PsiElement> {
    private static final Logger LOG = Logger.getInstance(SchemaEdge.class);
    public SchemaEdge(@NotNull DiagramNode<PsiElement> source, @NotNull DiagramNode<PsiElement> target, @NotNull DiagramRelationshipInfo relationship) {
        super(source, target, relationship);
    }
}
