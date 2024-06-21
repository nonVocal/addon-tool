package dev.nonvocal.infrastructure;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.swing.*;
import java.awt.*;


@Component (service = Images.class)
public class Images
{
    private ResourceAccessor resourceAccessor;


    @Activate
    public Images(@Reference ECTRService ectrService)
    {
        resourceAccessor = ectrService.getResourceAccessor();
    }

    public ImageIcon getImageIcon(String s, ResourceAccessor.IconSize iconSize)
    {
        return resourceAccessor.getImageIcon(s, iconSize);
    }

    public Image getImage(String s)
    {
        return resourceAccessor.getImage(s);
    }
}
