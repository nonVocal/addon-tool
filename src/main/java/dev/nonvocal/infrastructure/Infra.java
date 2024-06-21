package dev.nonvocal.infrastructure;

import com.dscsag.plm.spi.interfaces.ECTRService;

public class Infra
{
    private static final class Holder
    {
        private static Infra instance;
    }

    private final Logger logger;
    private final Config config;
    private final Images images;

    private Infra(ECTRService service)
    {
        this.logger = new Logger(service);
        this.config = new Config(service);
        this.images = new Images(service);
    }

    public static Infra initInstance(ECTRService service)
    {
        if (Holder.instance == null)
        {
            Holder.instance = new Infra(service);
        }
        return Holder.instance;
    }

    public Logger logger()
    {
        return logger;
    }

    public Config config()
    {
        return config;
    }

    public Images images()
    {
        return images;
    }

    public static Infra getInstance()
    {
        return Holder.instance;
    }

}
