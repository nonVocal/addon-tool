package dev.nonvocal.gui;

import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonTreeModel;

import javax.swing.*;
import java.awt.*;
import java.util.OptionalInt;

public class NavBar extends JTree
{
    private final AddonTreeModel model;

    public NavBar(AddonCollector.AddonCollection addons)
    {
        super(new AddonTreeModel(addons));
        this.setRootVisible(false);
        this.setShowsRootHandles(true);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));


        this.model = (AddonTreeModel) super.getModel();

        var fm = getFontMetrics(getFont());
        int widthRoot = fm.stringWidth(model.getRoot().toString());

        OptionalInt max = addons.bundles()
                .flatMap(bundle -> bundle.addons().stream())
                .mapToInt(a -> fm.stringWidth(a.domain() + a.name()))
                .max();

        int maxLength = widthRoot + max.getAsInt();

        setMinimumSize(new Dimension(maxLength, 100));
        setPreferredSize(new Dimension(maxLength, 100));
    }

    @Override
    public AddonTreeModel getModel()
    {
        return model;
    }
}