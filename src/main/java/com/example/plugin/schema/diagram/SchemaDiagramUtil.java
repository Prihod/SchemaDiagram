package com.example.plugin.schema.diagram;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SchemaDiagramUtil {
    private static final Logger LOG = Logger.getInstance(SchemaDiagramUtil.class);

    public static boolean isSchemaFileExtension(String filename) {
        return filename.endsWith(".schema.xml");
    }

    public static boolean isXmlSchemaFile(Object element) {
        if (element instanceof XmlFile file) {
            return isSchemaFileExtension(file.getName());
        }
        return false;
    }

    public static boolean isTagTable(Object element) {
        if (element instanceof XmlTag tag && tag.getName().equals("object")) {
            return true;
        }
        return false;
    }

    public static boolean isTagField(Object element) {
        if (element instanceof XmlTag tag && tag.getName().equals("field")) {
            return true;
        }
        return false;
    }

    public @NotNull
    static String getFieldName(@Nullable Object element) {
        String name = null;
        if (SchemaDiagramUtil.isTagField(element)) {
            name = ((XmlTag) element).getAttributeValue("key");
        }
        return StringUtil.notNullize(name);
    }

    public @NotNull
    static String getFieldPhpType(@Nullable Object element) {
        String name = null;
        if (SchemaDiagramUtil.isTagField(element)) {
            name = ((XmlTag) element).getAttributeValue("phptype");
        }
        return StringUtil.notNullize(name);
    }


    public static boolean isFieldPrimaryKey(Object element) {
        if (element instanceof XmlTag tag && isTagField(tag)) {
            return tag.getAttribute("index") != null && tag.getAttributeValue("index").equals("pk");
        }
        return false;
    }


    public @Nullable
    static String getPackageNameByTagTable(@NotNull XmlTag table) {
        if (table.getParent() != null) {
            return ((XmlTag) table.getParent()).getAttributeValue("package");
        }
        return null;
    }

    public @NotNull
    static String getTableName(@NotNull Object element) {
        String name = null;
        if (SchemaDiagramUtil.isTagTable(element)) {
            name = ((XmlTag) element).getAttributeValue("table");
        }
        return StringUtil.notNullize(name);
    }

    public @NotNull
    static String getTableClassName(@NotNull Object element) {
        String name = null;
        if (SchemaDiagramUtil.isTagTable(element)) {
            name = ((XmlTag) element).getAttributeValue("class");
        }
        return StringUtil.notNullize(name);
    }

    @NotNull
    public static List<XmlTag> getTableFields(PsiElement element, boolean onlyPrimaryKey) {
        List<XmlTag> list = new ArrayList<>();
        if (element != null) {
            list = new ArrayList<>(PsiTreeUtil.findChildrenOfType(element, XmlTag.class)
                    .stream()
                    .filter(tag -> {
                        if (isTagField(tag)) {
                            return onlyPrimaryKey ? isFieldPrimaryKey(tag) : true;
                        }
                        return false;
                    })
                    .toList());
        }
        return list;
    }

    public static XmlTag cloneXmlTag(XmlTag tag, Project project) {
        XmlElementFactory factory = XmlElementFactory.getInstance(project);
        return factory.createTagFromText(tag.getText());
    }

    public static List<XmlTag> cloneListXmlTags(List<XmlTag> list, Project project) {
        List<XmlTag> cloneList = new ArrayList<>();
        for (XmlTag tag : list) {
            cloneList.add(SchemaDiagramUtil.cloneXmlTag(tag, project));
        }
        return cloneList;
    }

    public @NotNull
    static XmlTag makeXmlTagPrimaryIndex(Project project) {
        XmlElementFactory factory = XmlElementFactory.getInstance(project);
        String text = "<field key=\"id\" dbtype=\"integer\" attributes=\"unsigned\" phptype=\"integer\" null=\"false\" default=\"0\" index=\"pk\"/>";
        return factory.createTagFromText(text);
    }


    public @Nullable
    static XmlTag makeXmlTagRelation(String dependency, String type, String alias, String className, String local, String foreign, String owner, Project project) {
        XmlElementFactory factory = XmlElementFactory.getInstance(project);
        String text = "<" + dependency + " alias=\"" + alias + "\" class=\"" + className + "\" local=\"" + local + "\" foreign=\"" + foreign + "\" cardinality=\"" + type + "\" owner=\"" + owner + "\" />";
        return factory.createTagFromText(text);
    }


    public @Nullable
    static XmlTag saveXMLTagTable(@NotNull XmlTag tag, @NotNull XmlFile file) {
        AtomicReference<XmlTag> savedTag = null;
        WriteCommandAction.runWriteCommandAction(file.getProject(), () -> {
            XmlDocument document = file.getDocument();
            if (document != null) {
                XmlTag rootTag = document.getRootTag();
                if (rootTag != null) {
                    savedTag.set((XmlTag) rootTag.add(tag));
                }
            }
        });
        commitXMLDocument(file);
        return savedTag.get();
    }

    public static void removeXmlTagFromFile(@NotNull XmlFile file, @NotNull XmlTag tag) {
        WriteCommandAction.runWriteCommandAction(file.getProject(), () -> {
            XmlDocument document = file.getDocument();
            if (document != null) {
                LOG.warn("document");
                XmlTag rootTag = document.getRootTag();
                if (rootTag != null) {
                    tag.delete();
                }
            }
            commitXMLDocument(file);
        });
    }


    public @Nullable
    static XmlTag getXmlTagAtIndex(XmlTag parent, int index) {
        XmlTag[] subTags = parent.getSubTags();
        if (index >= 0 && index < subTags.length) {
            return subTags[index];
        }
        return null;
    }

    public static void commitXMLDocument(@NotNull XmlFile file) {
        PsiDocumentManager.getInstance(file.getProject()).commitDocument(file.getViewProvider().getDocument());
    }

}
