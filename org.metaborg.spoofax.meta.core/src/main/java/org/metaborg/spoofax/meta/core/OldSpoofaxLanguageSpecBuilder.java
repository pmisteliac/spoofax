package org.metaborg.spoofax.meta.core;
//
//import java.io.IOException;
//import java.net.URL;
//
//import javax.annotation.Nullable;
//
//import org.apache.commons.vfs2.AllFileSelector;
//import org.apache.commons.vfs2.FileObject;
//import org.apache.commons.vfs2.FileSystemException;
//import org.apache.tools.ant.BuildListener;
//import org.metaborg.core.MetaborgException;
//import org.metaborg.core.action.CompileGoal;
//import org.metaborg.core.build.BuildInput;
//import org.metaborg.core.build.NewBuildInputBuilder;
//import org.metaborg.core.build.dependency.INewDependencyService;
//import org.metaborg.core.build.paths.ILanguagePathService;
//import org.metaborg.core.processing.ICancellationToken;
//import org.metaborg.spoofax.core.processing.ISpoofaxProcessorRunner;
//import org.metaborg.spoofax.core.project.ISpoofaxLanguageSpecPaths;
//import org.metaborg.spoofax.core.project.ISpoofaxLanguageSpecPathsService;
//import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfigWriter;
//import org.metaborg.spoofax.generator.language.LanguageSpecGenerator;
//import org.metaborg.spoofax.generator.project.LanguageSpecGeneratorScope;
//import org.metaborg.spoofax.meta.core.ant.IAntRunner;
//import org.metaborg.spoofax.meta.core.pluto.SpoofaxContext;
//import org.metaborg.spoofax.meta.core.pluto.build.main.GenerateSourcesBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.inject.Inject;

    /*
public class SpoofaxLanguageSpecBuilder {
    private static final Logger log = LoggerFactory.getLogger(SpoofaxLanguageSpecBuilder.class);

    private final INewDependencyService dependencyService;
    private final ILanguagePathService languagePathService;
    private final ISpoofaxProcessorRunner runner;
    private final ISpoofaxLanguageSpecConfigWriter languageSpecConfigWriter;
    private final ISpoofaxLanguageSpecPathsService languageSpecPathsService;

    private final NewMetaBuildAntRunnerFactory antRunner;


    @Inject public SpoofaxLanguageSpecBuilder(INewDependencyService dependencyService, ILanguagePathService languagePathService,
                                              ISpoofaxLanguageSpecPathsService languageSpecPathsService,
                                              ISpoofaxProcessorRunner runner, NewMetaBuildAntRunnerFactory antRunner,
                                              ISpoofaxLanguageSpecConfigWriter languageSpecConfigWriter) {
        this.dependencyService = dependencyService;
        this.languagePathService = languagePathService;
        this.languageSpecPathsService = languageSpecPathsService;
        this.runner = runner;
        this.antRunner = antRunner;
        this.languageSpecConfigWriter = languageSpecConfigWriter;
    }


    public void initialize(LanguageSpecBuildInput input) throws FileSystemException {
        ISpoofaxLanguageSpecPaths paths = this.languageSpecPathsService.get(input.languageSpec);
        paths.includeFolder().createFolder();
        paths.libFolder().createFolder();
        paths.generatedSourceFolder().createFolder();
        paths.generatedSyntaxFolder().createFolder();
    }

    public void generateSources(LanguageSpecBuildInput input, ISpoofaxLanguageSpecPaths paths) throws Exception {
        log.debug("Generating sources for {}", input.languageSpec.location());

        final LanguageSpecGenerator generator = new LanguageSpecGenerator(new LanguageSpecGeneratorScope(paths, input.config));
        generator.generateAll();

        // Store the configuration.
        this.languageSpecConfigWriter.write(input.languageSpec, input.config);

        // HACK: compile the main ESV file to make sure that packed.esv file is always available.
        final FileObject mainEsvFile = paths.mainEsvFile();
        if(mainEsvFile.exists()) {
            // @formatter:off
            final BuildInput buildInput =
                new NewBuildInputBuilder(input.languageSpec)
                .addSource(mainEsvFile)
                .addTransformGoal(new CompileGoal())
                .build(dependencyService, languagePathService);
            // @formatter:on
            runner.build(buildInput, null, null).schedule().block();
        }
    }

    public void compilePreJava(LanguageSpecBuildInput input, @Nullable URL[] classpaths, @Nullable BuildListener listener,
        @Nullable ICancellationToken cancellationToken) throws Exception {
        log.debug("Running pre-Java build for {}", input.languageSpec.location());

        for(IBuildStep buildStep : buildSteps) {
            buildStep.compilePreJava(input);
        }

        // final IAntRunner runner = antRunner.create(input, classpaths, listener);
        // runner.execute("generate-sources", cancellationToken);

        initPluto();
        try {
            plutoBuild(GenerateSourcesBuilder.request(new GenerateSourcesBuilder.Input(new SpoofaxContext(
                    input.settings))));
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new MetaborgException("Build failed", e);
        }
    }

    public void compilePostJava(LanguageSpecBuildInput input, @Nullable URL[] classpaths, @Nullable BuildListener listener,
        @Nullable ICancellationToken cancellationToken) throws Exception {
        log.debug("Running post-Java build for {}", input.languageSpec.location());

        final IAntRunner runner = antRunner.create(input, classpaths, listener);
        runner.execute("package", cancellationToken);
    }

    public void clean(ISpoofaxLanguageSpecPaths paths) throws IOException {
        log.debug("Cleaning {}", paths.rootFolder());

        final AllFileSelector selector = new AllFileSelector();
        paths.javaTransDirectory().delete(selector);
        paths.outputFolder().delete(selector);
        paths.generatedSourceDirectory().delete(selector);
        paths.cacheDirectory().delete(selector);
    }
}
    */
