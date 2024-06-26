package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.PlmPreferences;
import com.dscsag.plm.spi.interfaces.gui.*;
import dev.nonvocal.infrastructure.Config;

import java.awt.*;

public class OmfOpenConfig implements PluginFunction
{
    private final ECTRService ectrService;

    public OmfOpenConfig(ECTRService ectrService)
    {
        this.ectrService = ectrService;
    }

    @Override
    public PluginResponse actionPerformed(PluginRequest pluginRequest)
    {
        PlmPreferences plmPreferences = ectrService.getPlmPreferences();

        var editApp = plmPreferences.stringValue(Config.PREF_NAMESPACE, "config.edit.application");
//        if (editApp == null || editApp.isEmpty())
//            Desktop.getDesktop().browse();


        return PluginResponseFactory.infoResponse("Yeah!");
    }
}
