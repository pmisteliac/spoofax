package org.metaborg.spoofax.core.outline;

import javax.inject.Inject;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.outline.IOutline;
import org.metaborg.core.outline.IOutlineNode;
import org.metaborg.core.outline.Outline;
import org.metaborg.spoofax.core.dynamicclassloading.IBuilderInput;
import org.metaborg.spoofax.core.dynamicclassloading.IDynamicClassLoadingService;
import org.metaborg.spoofax.core.dynamicclassloading.api.IOutliner;

public class JavaOutlineFacet implements IOutlineFacet {
    public final String javaClassName;
    public final int expandTo;
    private @Inject IDynamicClassLoadingService dynamicClassLoadingService;


    public JavaOutlineFacet(String javaClassName, int expandTo) {
        this.javaClassName = javaClassName;
        this.expandTo = expandTo;
    }


    @Override public int getExpansionLevel() {
        return expandTo;
    }

    @Override public IOutline createOutline(FileObject source, IContext context, ILanguageComponent contributor,
        IBuilderInput input) throws MetaborgException {
        IOutliner outliner = dynamicClassLoadingService.loadClass(contributor, javaClassName, IOutliner.class);
        Iterable<IOutlineNode> outline = outliner.createOutline(context, input);
        if(outline == null) {
            return null;
        }
        return new Outline(outline, getExpansionLevel());
    }
}
