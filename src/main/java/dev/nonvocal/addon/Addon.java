package dev.nonvocal.addon;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nonvocal.addon.module.ModuleInfo;
import org.eclipse.jdt.annotation.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class Addon
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String domain;
    private String name;
    private final Collection<String> jars;
    private final Collection<String> plugins;
    private ModuleInfo moduleInfo;

    private final Path addonPath;

    private final boolean enabled;

    public Addon(@NonNull Path addonPath)
    {
        this.addonPath = addonPath;

        this.enabled = Files.notExists(addonPath.resolve("disabled"));
        this.domain = addonPath.getName(addonPath.getNameCount() - 2).toString();

        this.jars = new ArrayList<>();
        this.plugins = new ArrayList<>();
        try (
                Stream<Path> classesStream = Files.list(addonPath.resolve("classes"));
                Stream<Path> pluginStream = Files.list(addonPath.resolve("plugins"));
        )
        {
            classesStream.forEach(path -> jars.add(path.getFileName().toString()));
            pluginStream.forEach(path -> plugins.add(path.getFileName().toString()));
            if (Files.exists(addonPath.resolve("module.information")))
            {
                moduleInfo = MAPPER.readValue(addonPath.resolve("module.information").toFile(), ModuleInfo.class);
                name = moduleInfo.name();
            }
        } catch (IOException e)
        {
            //
        }

    }

    public String domain()
    {
        return domain;
    }

    public String name()
    {
        return name;
    }

    public Collection<String> jars()
    {
        return jars;
    }

    public Collection<String> plugins()
    {
        return plugins;
    }

    public ModuleInfo moduleInfo()
    {
        return moduleInfo;
    }

    public Path addonPath()
    {
        return addonPath;
    }

    public boolean enabled()
    {
        return enabled;
    }

    public void enable()
    {
        try
        {
            Files.deleteIfExists(addonPath.resolve("disabled"));
        } catch (IOException e)
        {
            // ignore
        }
    }

    public void disable()
    {
        try
        {
            Files.createFile(addonPath.resolve("disabled"));
        } catch (IOException e)
        {
            // ignore
        }
    }
}
