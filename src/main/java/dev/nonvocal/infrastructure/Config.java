package dev.nonvocal.infrastructure;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.PlmPreference;
import com.dscsag.plm.spi.interfaces.commons.PlmPreferences;

@Component (service = Config.class)
public class Config
{
  public static final String PREF_NAMESPACE = "dev.nonvocal.addon.tool";


  private final PlmPreferences plmPrefs;

  @Activate
  public Config(@Reference ECTRService ectrService)
  {
    this.plmPrefs = ectrService.getPlmPreferences();
  }

  public PlmPreferences getPlmPrefs()
  {
    return plmPrefs;
  }
}
