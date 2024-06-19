package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.gui.PluginFunction;
import com.dscsag.plm.spi.interfaces.gui.PluginFunctionService;
import org.osgi.service.component.annotations.Component;

@Component(service = PluginFunctionService.class)
public class FunctionProvider implements PluginFunctionService
{
    @Override
    public PluginFunction getPluginFunction(String s)
    {
        if (s.equals("fnc.edit.addons"))
            return new OmfEditAddons();

        return null;
    }
}
