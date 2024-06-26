package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;
import dev.nonvocal.addon.AddonCollection;
import dev.nonvocal.util.CollectionUtils;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NavBarList extends JPanel
{
    private static final DefaultTreeCellRenderer forColors = new DefaultTreeCellRenderer();

    private Consumer<Addon> onAddon;
    private Consumer<AddonBundle> onBundle;

    private final Map<String, AddonBundle> bundles;
    private final MouseSelectionListener selectionListener = new MouseSelectionListener();
    //    private final AddonCollector.AddonCollection addons;
    private final AddonCollection addons;

    private final ResourceAccessor resourceAccessor;

    //    public NavBarList(AddonCollector.AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    public NavBarList(AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.addons = addons;
        this.resourceAccessor = resourceAccessor;

        bundles = addons.bundles().collect(Collectors.toMap(AddonBundle::domain, Function.identity()));

        for (String domain : addons.domains())
        {
            Collection<Addon> domainAddons = addons.get(domain);
            if (CollectionUtils.hasNoItems(domainAddons))
                continue;


//            for (int i = 0; i < 25; i++)
            for (Addon domainAddon : domainAddons)
            {
                AddonNavBarWidget addonNavBarWidget = new AddonNavBarWidget(resourceAccessor, domainAddon);
                this.add(addonNavBarWidget);
            }
        }

        addMouseListener(selectionListener);

    }

    public void addSelectAddon(Consumer<Addon> r)
    {
        this.onAddon = r;

        var compCount = getComponentCount();

        if (compCount > 0)
        {
            for (int i = 0; i < compCount; i++)
            {
                Component first = getComponent(i);
                if (first instanceof AddonNavBarWidget widget)
                {
                    selectionListener.setSelectedColors(widget);
                    selectionListener.selected = widget;

                    onAddon.accept(widget.addon());

                    break;
                }
            }
        }
    }

    public void addSelectBundle(Consumer<AddonBundle> r)
    {
        this.onBundle = r;
    }

    public void filter(String filterText)
    {
        selectionListener.clearSelection();
        removeAll();

        AddonCollection collection = new AddonCollection();
        addons.bundles().flatMap(bundle -> bundle.addons().stream())
                .filter(addon -> addon.name().toUpperCase().contains(filterText.toUpperCase()))
                .forEach(a -> collection.addAddon(a.domain(), a));

        buildPanel(collection, resourceAccessor);
        updateUI();
    }

    //    private void buildPanel(AddonCollector.AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    private void buildPanel(AddonCollection addons, @NonNull ResourceAccessor resourceAccessor)
    {
        for (String domain : addons.domains())
        {
            Collection<Addon> domainAddons = addons.get(domain);
            if (CollectionUtils.hasNoItems(domainAddons))
                continue;


//            for (int i = 0; i < 25; i++)
            for (Addon domainAddon : domainAddons)
            {
                AddonNavBarWidget addonNavBarWidget = new AddonNavBarWidget(resourceAccessor, domainAddon);
                this.add(addonNavBarWidget);
            }
        }
    }

    private class MouseSelectionListener extends MouseAdapter
    {
        Component selected = null;

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                Component componentAt = getComponentAt(e.getPoint());
                if (componentAt instanceof AddonNavBarWidget addon)
                {
                    if (onAddon != null)
                        onAddon.accept(addon.addon());

                    if (selected != null)
                        setNonSelectedColors(selected);

                    setSelectedColors(componentAt);
                    selected = componentAt;

                }
                else if (componentAt instanceof JLabel label)
                {
                    if (onBundle != null)
                        onBundle.accept(bundles.get(label.getText()));

                    if (selected != null)
                        setNonSelectedColors(selected);

                    setSelectedColors(componentAt);

                    selected = componentAt;
                }
            }
        }

        void setNonSelectedColors(Component c)
        {
            var nonSelectionBackground = UIManager.getColor("Panel.background");
            var nonSelectionTextColor = forColors.getTextNonSelectionColor();

            c.setForeground(nonSelectionTextColor);

            if (c instanceof JLabel)
            {
                Graphics graphics = c.getGraphics();
                graphics.setColor(nonSelectionBackground);
                graphics.drawRect(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            }
            else
                c.setBackground(nonSelectionBackground);
        }

        void setSelectedColors(Component c)
        {
            var selectionBackground = forColors.getBackgroundSelectionColor();
            var selectionTextColor = forColors.getTextSelectionColor();

            c.setForeground(selectionTextColor);

            if (c instanceof JLabel)
            {
                Graphics graphics = c.getGraphics();
                graphics.setColor(selectionBackground);
                graphics.drawRect(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            }
            else
                c.setBackground(selectionBackground);
        }

        void clearSelection()
        {
            if (selected != null)
            {
                setNonSelectedColors(selected);
                selected = null;
            }
        }
    }
}
