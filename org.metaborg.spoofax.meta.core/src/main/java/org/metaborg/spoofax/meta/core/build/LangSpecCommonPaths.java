package org.metaborg.spoofax.meta.core.build;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.build.CommonPaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LangSpecCommonPaths extends CommonPaths {
    public LangSpecCommonPaths(FileObject root) {
        super(root);
    }


    /* Shared output directories */

    /**
     * @return Target output directory for compiled Java classes.
     */
    public FileObject targetClassesDir() {
        return resolve(targetDir(), "classes");
    }

    /**
     * @return Target output directory for compiled Java classes.
     */
    public FileObject targetTestClassesDir() {
        return resolve(targetDir(), "test-classes");
    }

    /**
     * @return Target output directory for compiled MetaBorg artifacts (Stratego JAR, parse table, etc.). All compiled
     *         artifacts that should be included with the language go into this directory.
     */
    public FileObject targetMetaborgDir() {
        return resolve(targetDir(), "metaborg");
    }


    /* Stratego */

    /**
     * @return Generated Stratego Java directory, generated from Stratego definition.
     */
    public FileObject strSrcGenJavaTransDir() {
        return resolve(srcGenDir(), "stratego-java", "trans");
    }

    /**
     * @return Target output directory for compiled Stratego Java classes.
     */
    public FileObject strTargetClassesTransDir() {
        return resolve(targetClassesDir(), "trans");
    }

    /**
     * @param languageId
     *            Identifier of the language.
     * @return Target output directory for compiled Stratego Java strategy classes.
     */
    public FileObject strTargetClassesJavaStratDir(String languageId) {
        return resolve(targetClassesDir(), strJavaStratPkgPath(languageId));
    }


    /* DynSem */

    /**
     * @return Target output directory for compiled manual DynSem interpreter classes.
     */
    public FileObject dsTargetClassesManualDir() {
        return resolve(targetClassesDir(), "ds", "manual", "interpreter");
    }

    /**
     * @return Target output directory for compiled generated DynSem interpreter classes.
     */
    public FileObject dsTargetClassesGenerateDir() {
        return resolve(targetClassesDir(), "ds", "generated", "interpreter");
    }


    /* Pluto */

    /**
     * @return Target output directory for pluto build information.
     */
    public FileObject plutoBuildInfoDir() {
        return resolve(targetDir(), "pluto");
    }

    @Override
    public Collection<FileObject> javaSrcDirs() {
        final Collection<FileObject> dirs = new ArrayList<>();
        dirs.addAll(super.javaSrcDirs());
        dirs.addAll(Arrays.asList(
                // Add all Java source folders here too!
                // These must be the package root folders.
                strSrcGenJavaTransDir()
        ));
        return dirs;
    }
}