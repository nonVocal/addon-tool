package dev.nonvocal;

import com.dscsag.plm.spi.NotificationEventConstants;
import dev.nonvocal.infrastructure.Capabilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.Dictionary;
import java.util.Hashtable;

public class Activator implements BundleActivator
{
    private ServiceRegistration eventHandlerServiceRegistration;


    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        String[] topics = {
                NotificationEventConstants.CONFIGURATION_CHANGED,
                NotificationEventConstants.CONFIGURATION_CHANGED_PATH
        };

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(EventConstants.EVENT_TOPIC, topics);
        eventHandlerServiceRegistration = bundleContext.registerService(EventHandler.class.getName(), new EventListener(), properties);

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
        if (eventHandlerServiceRegistration != null)
            bundleContext.ungetService(eventHandlerServiceRegistration.getReference());
    }

    private class EventListener implements EventHandler
    {

        @Override
        public void handleEvent(Event event)
        {
            String topic = event.getTopic();
            if (topic.equals(NotificationEventConstants.CONFIGURATION_CHANGED) || topic.equals(NotificationEventConstants.CONFIGURATION_CHANGED_PATH))
            {
                Capabilities.usable = false;
            }
        }
    }
}
