package org.metaborg.spoofax.meta.core.pluto.build.main;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.metaborg.spoofax.meta.core.config.StrategoFormat;
import org.metaborg.spoofax.meta.core.pluto.SpoofaxBuilder;
import org.metaborg.spoofax.meta.core.pluto.SpoofaxBuilderFactory;
import org.metaborg.spoofax.meta.core.pluto.SpoofaxBuilderFactoryFactory;
import org.metaborg.spoofax.meta.core.pluto.SpoofaxContext;
import org.metaborg.spoofax.meta.core.pluto.SpoofaxInput;
import org.metaborg.spoofax.meta.core.pluto.build.misc.CopyPattern;
import org.metaborg.spoofax.meta.core.pluto.stamp.DirectoryModifiedStamper;

import com.google.common.collect.Lists;

import build.pluto.builder.BuildRequest;
import build.pluto.buildjava.JarBuilder;
import build.pluto.dependency.Origin;
import build.pluto.output.None;
import build.pluto.stamp.FileExistsStamper;

public class PackageBuilder extends SpoofaxBuilder<PackageBuilder.Input, None> {
    public static class Input extends SpoofaxInput {
        private static final long serialVersionUID = -2379365089609792204L;

        public final Origin generateSourcesOrigin;

        public final StrategoFormat strFormat;
        public final @Nullable File strJavaStratFile;

        public final List<File> strJavaStratIncludeDirs;


        public Input(SpoofaxContext context, Origin generateSourcesOrigin, StrategoFormat strFormat,
            @Nullable File strJavaStratFile, List<File> strJavaStratIncludeDirs) {
            super(context);
            this.generateSourcesOrigin = generateSourcesOrigin;
            this.strFormat = strFormat;
            this.strJavaStratFile = strJavaStratFile;
            this.strJavaStratIncludeDirs = strJavaStratIncludeDirs;
        }
    }

    public static SpoofaxBuilderFactory<Input, None, PackageBuilder> factory =
        SpoofaxBuilderFactoryFactory.of(PackageBuilder.class, Input.class);


    public PackageBuilder(Input input) {
        super(input);
    }


    public static BuildRequest<Input, None, PackageBuilder, SpoofaxBuilderFactory<Input, None, PackageBuilder>>
        request(Input input) {
        return new BuildRequest<>(factory, input);
    }

    public static Origin origin(Input input) {
        return Origin.from(request(input));
    }


    @Override protected String description(Input input) {
        return "Package";
    }

    @Override public File persistentPath(Input input) {
        return context.depPath("package.dep");
    }

    @Override protected None build(Input input) throws Throwable {
        final File targetMetaborgDir = toFile(paths.targetMetaborgDir());
        final File targetClassesDir = toFile(paths.targetClassesDir());

        if(input.strFormat == StrategoFormat.jar) {
            final File strJavaTransDir = toFile(paths.strSrcGenJavaTransDir());
            final File strClassesTransDir = toFile(paths.strTargetClassesTransDir());

            // Copy .pp.af and .tbl to JAR target directory, so that they get included in the JAR file.
            // Required for being able to import-term those files from Stratego code.
            final CopyPattern.Input copyPatternInput = new CopyPattern.Input(strJavaTransDir, strClassesTransDir,
                ".+\\.(?:tbl|pp\\.af)", input.generateSourcesOrigin, context.baseDir, context.depDir);
            final Origin copyPatternOrigin = CopyPattern.origin(copyPatternInput);
            requireBuild(copyPatternOrigin);

            final String jarName = "stratego.jar";
            final File jarFile = FileUtils.getFile(targetMetaborgDir, jarName);
            final File depPath = FileUtils.getFile(context.depDir, jarName + ".dep");
            jar(jarFile, targetClassesDir, copyPatternOrigin, depPath, strClassesTransDir);
        }

        if(input.strJavaStratFile != null) {
            require(input.strJavaStratFile, FileExistsStamper.instance);
            if(!input.strJavaStratFile.exists()) {
                throw new IOException(
                    "Main Stratego Java strategies file at " + input.strJavaStratFile + " does not exist");
            }

            final String jarName = "stratego-javastrat.jar";
            final File jarFile = FileUtils.getFile(targetMetaborgDir, jarName);
            final File depPath = FileUtils.getFile(context.depDir, jarName + ".dep");
            jar(jarFile, targetClassesDir, null, depPath, input.strJavaStratIncludeDirs);
        }

        return None.val;
    }

    public void jar(File jarFile, File baseDir, @Nullable Origin origin, @Nullable File depPath, File... paths)
        throws IOException {
        jar(jarFile, baseDir, origin, depPath, Lists.newArrayList(paths));
    }

    public void jar(File jarFile, File baseDir, @Nullable Origin origin, @Nullable File depPath, Collection<File> paths)
        throws IOException {
        final Collection<JarBuilder.Entry> fileEntries = Lists.newLinkedList();

        for(File path : paths) {
            require(path, new DirectoryModifiedStamper());
            final Collection<File> files = findFiles(path);
            for(final File classFile : files) {
                final String relative = relativize(classFile, baseDir);
                // Ignore files that are not relative to the base directory.
                if(relative != null) {
                    // Convert \ to / on Windows; ZIP/JAR files must use / for paths.
                    // HACK: this should be fixed in the JarBuilder.
                    final String forwardslashRelative = relative.replace('\\', '/');
                    fileEntries.add(new JarBuilder.Entry(forwardslashRelative, classFile));
                }
            }
        }

        requireBuild(JarBuilder.factory, new JarBuilder.Input(jarFile, fileEntries, origin, depPath));
    }

    private @Nullable String relativize(File path, File base) {
        final String relative = FilenameUtils.normalize(base.toPath().relativize(path.toPath()).toString());
        if(relative == null || relative.equals(""))
            return null;
        return relative;
    }

    private Collection<File> findFiles(File directory) {
        if(!directory.isDirectory())
            return Collections.emptyList();
        return FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }
}