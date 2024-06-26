package dev.nonvocal.omf;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.gui.PluginFunction;
import com.dscsag.plm.spi.interfaces.gui.PluginFunctionService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component
public class FunctionProvider implements PluginFunctionService
{
    private final Map<String, PluginFunction> functions = new HashMap<>();

    @Activate
    public FunctionProvider(@Reference ECTRService service)
    {
        functions.put("fnc.edit.addons", new OmfEditAddons(service));
        functions.put("fnc.open.config", new OmfOpenConfig(service));
    }


    @Override
    public PluginFunction getPluginFunction(String s)
    {
//        if (s.equals("fnc.edit.addons"))
//            return new OmfEditAddons(service);
//
//        return null;

        return functions.get(s);
    }
}
