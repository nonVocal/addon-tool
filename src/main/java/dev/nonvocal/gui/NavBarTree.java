package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.addon.AddonCollection;
import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonTreeModel;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import java.awt.*;
import java.util.OptionalInt;

public class NavBarTree extends JTree
{
    private final AddonTreeModel model;

//    public NavBarTree(AddonCollector.AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    public NavBarTree(AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    {
        super(new AddonTreeModel(addons));
        this.setRootVisible(false);
        this.setShowsRootHandles(true);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setCellRenderer(new NavBarRenderer(resourceAccessor));

        this.model = (AddonTreeModel) super.getModel();

        var fm = getFontMetrics(getFont());
        int widthRoot = fm.stringWidth(model.getRoot().toString());

        OptionalInt max = addons.bundles()
                .flatMap(bundle -> bundle.addons().stream())
                .mapToInt(a -> fm.stringWidth(a.domain() + a.name()))
                .max();

        max.ifPresent(maxStringWidth ->
        {
            int maxLength = widthRoot + maxStringWidth;
            setMinimumSize(new Dimension(maxLength, 100));
            setPreferredSize(new Dimension(maxLength, 100));
        });
    }

    @Override
    public AddonTreeModel getModel()
    {
        return model;
    }
}