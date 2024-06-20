package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.gui.PluginFunction;
import com.dscsag.plm.spi.interfaces.gui.PluginFunctionService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class FunctionProvider implements PluginFunctionService
{
    @Reference ECTRService service;

    @Override
    public PluginFunction getPluginFunction(String s)
    {
        if (s.equals("fnc.edit.addons"))
            return new OmfEditAddons(service);

        return null;
    }
}
