package com.example.plugin.schema.icons;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.Field;

public class SchemaIcons {
    private static final Logger LOG = Logger.getInstance(SchemaIcons.class);
    public static final Icon EMPTY_HIDE = IconUtil.getEmptyIcon(false);

    public static final Icon TABLE = IconLoader.getIcon("/icons/table.svg", SchemaIcons.class);
}
