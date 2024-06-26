package dev.nonvocal.addon;

import com.dscsag.plm.spi.interfaces.commons.PlmPreferences;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nonvocal.addon.module.BackendDependency;
import dev.nonvocal.addon.module.Dependency;
import dev.nonvocal.addon.module.ModuleInfo;
import dev.nonvocal.gui.AddonEnableListener;
import dev.nonvocal.infrastructure.Config;
import org.eclipse.jdt.annotation.NonNull;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Addon
{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String disabled = "DISABLED";

    private PlmLogger logger;
    private PlmPreferences preferences;

    private final String domain;
    private String name;
    private final Collection<String> jars;
    private final Collection<String> plugins;
    private ModuleInfo moduleInfo;

    private final Path addonPath;

    private boolean enabled;

    private List<AddonEnableListener> enableListeners = new ArrayList<>();

    public Addon(@NonNull Path addonPath, @NonNull PlmLogger logger, @NonNull PlmPreferences preferences)
    {
        this.addonPath = addonPath;
        this.logger = logger;
        this.preferences = preferences;

        this.enabled = Files.notExists(addonPath.resolve(disabled));
        this.domain = addonPath.getName(addonPath.getNameCount() - 2).toString();

        this.jars = new ArrayList<>();
        this.plugins = new ArrayList<>();
        try
        //            (
        //                Stream<Path> classesStream = Files.list(addonPath.resolve("classes"));
        //                Stream<Path> pluginStream = Files.list(addonPath.resolve("plugins"));
        //        )
        {
            //            classesStream.forEach(path -> jars.add(path.getFileName().toString()));
            //            pluginStream.forEach(path -> plugins.add(path.getFileName().toString()));
            if (Files.exists(addonPath.resolve("module.information")))
            {
                moduleInfo = MAPPER.readValue(addonPath.resolve("module.information").toFile(), ModuleInfo.class);
                name = moduleInfo.name();

                logger.debug(moduleInfo.toString());
            }
            else
            {
                name = addonPath.getFileName().toString();

                logger.debug("No module.information file available");
            }
        } catch (IOException e)
        {
            logger.error(e);
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

    public Path addonPath()
    {
        return addonPath;
    }

    public String version()
    {
        if (moduleInfo == null)
            return "UNKNOWN";

        return moduleInfo.version();
    }

    public Collection<Dependency> dependencies()
    {
        if (moduleInfo == null)
            return List.of();

        return moduleInfo.dependencies();
    }

    public Collection<BackendDependency> backendDependencies()
    {
        if (moduleInfo == null)
            return List.of();

        return moduleInfo.backendDependencies();
    }

    public boolean enabled()
    {
        return enabled;
    }

    public void enable()
    {
        try
        {
            Files.deleteIfExists(addonPath.resolve(disabled));
            enabled = true;

            fireEnabled();
        } catch (IOException e)
        {
            logger.error(e);
        }
    }

    public void disable()
    {
        try
        {
            Files.createFile(addonPath.resolve(disabled));
            enabled = false;

            fireDisabled();
        } catch (IOException e)
        {
            logger.error(e);
        }
    }

    private void fireEnabled()
    {
        for (AddonEnableListener listener : enableListeners)
            listener.addonEnablementChanged(true);
    }

    private void fireDisabled()
    {
        for (AddonEnableListener listener : enableListeners)
            listener.addonEnablementChanged(false);
    }

    public void addEnableListener(AddonEnableListener listener)
    {
        enableListeners.add(listener);
    }

    public void removeEnableListener(AddonEnableListener listener)
    {
        enableListeners.remove(listener);
    }

    public void openConfig() throws IOException
    {
        var editApp = preferences.stringValue(Config.PREF_NAMESPACE, "config.edit.application");
        if (editApp == null || editApp.isEmpty())
            Desktop.getDesktop().browse(addonPath.toUri());
        else
        {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/C", editApp, addonPath.toString());
            processBuilder.start();
        }
    }
}
