package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.gui.PlmStatusLineMode;
import com.dscsag.plm.spi.interfaces.gui.PluginFunction;
import com.dscsag.plm.spi.interfaces.gui.PluginRequest;
import com.dscsag.plm.spi.interfaces.gui.PluginResponse;
import com.dscsag.plm.spi.interfaces.gui.PluginResponseFactory;

public class OmfEditAddons implements PluginFunction
{
    ECTRService service;

    public OmfEditAddons(ECTRService service)
    {
        this.service = service;
    }

    @Override
    public PluginResponse actionPerformed(PluginRequest pluginRequest)
    {
        var statusline = service.getStatusLine();
        statusline.setText(PlmStatusLineMode.SUCCESS, "Called Addon Manager");
        return PluginResponseFactory.infoResponse("Yeah!");
    }
}
