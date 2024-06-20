package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.gui.PlmStatusLineMode;
import com.dscsag.plm.spi.interfaces.gui.PluginFunction;
import com.dscsag.plm.spi.interfaces.gui.PluginRequest;
import com.dscsag.plm.spi.interfaces.gui.PluginResponse;
import com.dscsag.plm.spi.interfaces.gui.PluginResponseFactory;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

import dev.nonvocal.gui.MainUI;

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

        MainUI mainUI = new MainUI(service);

        return PluginResponseFactory.infoResponse("Yeah!");
    }
}
