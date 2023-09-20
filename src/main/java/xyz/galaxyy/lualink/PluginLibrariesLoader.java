package xyz.galaxyy.lualink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public final class PluginLibrariesLoader implements PluginLoader {

    private final Gson gson = new GsonBuilder().setLenient().create();

    @Override
    public void classloader(final @NotNull PluginClasspathBuilder classpathBuilder) {
        // Loading internal libraries.
        try (final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("paper-libraries.json")) {
            // Extracting file contents to new MavenLibraryResolver instance.
            final MavenLibraryResolver iLibraries = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), PluginLibraries.class)
                    .toMavenLibraryResolver();
            // Adding library(-ies) to the PluginClasspathBuilder.
            classpathBuilder.addLibrary(iLibraries);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final File eLibrariesFile = new File(classpathBuilder.getContext().getDataDirectory().toFile(), "libraries.json");
        // Copying default file to "plugins/LuaLink/..."
        if (!eLibrariesFile.exists()) {
            eLibrariesFile.getParentFile().mkdirs();
            try (final InputStream stream  = this.getClass().getClassLoader().getResourceAsStream("libraries.json")) {
                Files.copy(stream, eLibrariesFile.toPath());
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Loading external (user-specified) libraries.
        try {
            // Extracting file contents to new MavenLibraryResolver instance.
            final MavenLibraryResolver eDependencies = gson.fromJson(new InputStreamReader(new FileInputStream(eLibrariesFile), StandardCharsets.UTF_8), PluginLibraries.class)
                    .toMavenLibraryResolver();
            // Adding library(-ies) to the PluginClasspathBuilder.
            classpathBuilder.addLibrary(eDependencies);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private record PluginLibraries(Map<String, String> repositories, List<String> dependencies) {

        public MavenLibraryResolver toMavenLibraryResolver() {
            final MavenLibraryResolver resolver = new MavenLibraryResolver();
            // ...
            repositories.entrySet().stream()
                    .map(entry -> new RemoteRepository.Builder(entry.getKey(), "default", entry.getValue()).build())
                    .forEach(resolver::addRepository);
            dependencies.stream()
                    .map(value -> new Dependency(new DefaultArtifact(value), null))
                    .forEach(resolver::addDependency);
            // ...
            return resolver;
        }
    }
}