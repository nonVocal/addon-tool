package dev.nonvocal.addon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import com.dscsag.plm.spi.interfaces.commons.PlmPreferences;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.PlmEnvironment;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

@Component
public class AddonCollector
{
  private final PlmLogger logger;
  private final PlmPreferences preferences;

  private final String instDir;
  private final Path installationDirectory;

  @Activate
  public AddonCollector(@Reference ECTRService ectrService)
  {
    this.logger = ectrService.getPlmLogger();
    this.preferences = ectrService.getPlmPreferences();
    PlmEnvironment environment = ectrService.getEnvironment();
    Map<String, String> scriptEnvironment = environment.getScriptEnvironment();

    instDir = scriptEnvironment.get("system.start.instdir");

    logger.debug(instDir);

    installationDirectory = Path.of(instDir);
  }

  public AddonCollection collectAddons()
  {
    Collection<Addon> addons = collectionAddons("addons");
    Collection<Addon> applications = collectionAddons("applications");

    AddonCollection collection = new AddonCollection();
//    collection.addons.put("addons", addons);
//    collection.addons.put("application", applications);

    addons.forEach(addon -> collection.addAddon("addons", addon));
    applications.forEach(addon -> collection.addAddon("applications", addon));

    if (logger.isDebug())
    {
      logger.debug("Found addon:");
      collection.bundles()
          .forEach(bundle -> {
            logger.debug(bundle.domain());
            for (Addon addon : bundle.addons())
              logger.debug("-> " + addon.name());
          });
    }

    return collection;
  }

  private Collection<Addon> collectionAddons(String domain)
  {
    Path domainPath = installationDirectory.resolve(domain);
    try (Stream<Path> stream = Files.list(domainPath))
    {
      return stream
          .map(p -> new Addon(p, logger, preferences))
          .toList();
    }
    catch (IOException e)
    {
      logger.error(e);
      return List.of();
    }
  }

//  public static class AddonCollection
//  {
//    private final Map<String, Collection<Addon>> addons = new HashMap<>();
//
//    public Collection<Addon> get(String domain)
//    {
//      return addons.getOrDefault(domain, List.of());
//    }
//
//    public Collection<String> domains()
//    {
//      return addons.keySet();
//    }
//
//    public Stream<AddonBundle> bundles()
//    {
//      return addons.entrySet().stream()
//          .map(AddonBundle::of);
//    }
//
//    private void addAddon(String domain, Addon addon)
//    {
//      addons.computeIfAbsent(domain, k -> new ArrayList<>()).add(addon);
//    }
//  }
}
