package com.example.plugin.schema.diagram;

import com.intellij.diagram.AbstractDiagramNodeContentManager;
import com.intellij.diagram.DiagramCategory;
import org.jetbrains.annotations.NotNull;

public class SchemaNodeContentManager extends AbstractDiagramNodeContentManager {
    @Override
    public DiagramCategory @NotNull [] getContentCategories() {
        return new DiagramCategory[0];
    }
}
