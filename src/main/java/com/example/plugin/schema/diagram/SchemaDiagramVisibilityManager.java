package com.example.plugin.schema.diagram;

import com.intellij.diagram.AbstractUmlVisibilityManager;
import com.intellij.diagram.VisibilityLevel;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class SchemaDiagramVisibilityManager extends AbstractUmlVisibilityManager {
    private static final Logger LOG = Logger.getInstance(SchemaDiagramVisibilityManager.class);

    @Override
    public VisibilityLevel @NotNull [] getVisibilityLevels() {
        return VisibilityLevel.EMPTY_ARRAY;
    }

    @Override
    public @Nullable VisibilityLevel getVisibilityLevel(@Nullable Object o) {
        return null;
    }

    @Override
    public @NotNull Comparator<VisibilityLevel> getComparator() {
        return VisibilityLevel.DUMMY_COMPARATOR;
    }
}
