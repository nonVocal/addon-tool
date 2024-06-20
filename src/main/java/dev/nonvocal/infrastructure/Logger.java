package dev.nonvocal.infrastructure;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

@Component(service = Logger.class)
public class Logger
{
  private final PlmLogger logger;

  @Activate
  public Logger(@Reference ECTRService ectrService)
  {
      logger = ectrService.getPlmLogger();
  }

  public void trace(String s)
  {
    logger.trace(s);
  }

  public void debug(String s)
  {
    logger.debug(s);
  }

  public void warning(String s)
  {
    logger.warning(s);
  }

  public void error(Throwable throwable)
  {
    logger.error(throwable);
  }

  public boolean isVerbose()
  {
    return logger.isVerbose();
  }

  public void verbose(String s)
  {
    logger.verbose(s);
  }

  public void error(String s)
  {
    logger.error(s);
  }

  public boolean isDebug()
  {
    return logger.isDebug();
  }
}
