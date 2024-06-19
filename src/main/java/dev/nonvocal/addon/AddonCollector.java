package dev.nonvocal.addon;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.PlmEnvironment;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component(service = AddonCollector.class)
public class AddonCollector
{
    private final String instDir;
    private final Path installationDirectory;

    @Activate
    public AddonCollector(@Reference ECTRService ectrService)
    {
        PlmEnvironment environment = ectrService.getEnvironment();
        Map<String, String> scriptEnvironment = environment.getScriptEnvironment();

        instDir = scriptEnvironment.get("system.instdir");

        installationDirectory = Path.of(instDir);
    }

    public AddonCollection collectAddons()
    {
        Collection<Addon> addons = collectionAddons("addons");
        Collection<Addon> applications = collectionAddons("applications");

        AddonCollection collection = new AddonCollection();
        collection.addons.put("addons", addons);
        collection.addons.put("application", applications);

        return collection;
    }
    
    private Collection<Addon> collectionAddons(String domain)
    {
        Path domainPath = installationDirectory.resolve(domain);
        try (Stream<Path> stream = Files.list(domainPath))
        {
            return stream
                    .map(Addon::new)
                    .toList();
        } catch (IOException e)
        {
            return List.of();
        }
    }

    public static class AddonCollection
    {
        private Map<String, Collection<Addon>> addons;

        public Collection<Addon> get(String domain)
        {
            return addons.getOrDefault(domain, List.of());
        }

        public Collection<String> domains()
        {
            return addons.keySet();
        }

        public Stream <AddonBundle> bundles()
        {
            return addons.entrySet().stream()
                    .map(AddonBundle::of);
        }

        private void addAddon(String domain, Addon addon)
        {
            addons.computeIfAbsent(domain, k -> new ArrayList<>()).add(addon);
        }

        public record AddonBundle(String domain, Collection<Addon> addons)
        {
            static AddonBundle of(Map.Entry<String, Collection<Addon>> entry)
            {
                return new AddonBundle(entry.getKey(), entry.getValue());
            }
        }
    }
}
