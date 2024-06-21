package dev.nonvocal.addon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.nonvocal.addon.module.BackendDependency;
import dev.nonvocal.addon.module.Dependency;
import org.eclipse.jdt.annotation.NonNull;

import com.dscsag.plm.spi.interfaces.logging.PlmLogger;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.nonvocal.addon.module.ModuleInfo;

public class Addon
{
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final String disabled = "DISABLED";

  private PlmLogger logger;

  private final String domain;
  private String name;
  private final Collection<String> jars;
  private final Collection<String> plugins;
  private ModuleInfo moduleInfo;

  private final Path addonPath;

  private boolean enabled;

  public Addon(@NonNull Path addonPath, @NonNull PlmLogger logger)
  {
    this.logger = logger;
    this.addonPath = addonPath;

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
    }
    catch (IOException e)
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
    }
    catch (IOException e)
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
    }
    catch (IOException e)
    {
      logger.error(e);
    }
  }
}
