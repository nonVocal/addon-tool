package dev.nonvocal;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.commons.*;
import com.dscsag.plm.spi.interfaces.gui.PlmStatusLine;
import com.dscsag.plm.spi.interfaces.gui.PlmStatusLineMode;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;
import com.dscsag.plm.spi.interfaces.rfc.RfcExecutor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.formdev.flatlaf.FlatDarkLaf;
import dev.nonvocal.gui.MainUI;
import dev.nonvocal.infrastructure.Infra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main
{
    public static void main(String[] args) throws JsonProcessingException
    {
////        Path p = Paths.get("C:\\Users\\benda\\IdeaProjects\\addon-tool\\src\\main\\java\\dev\\nonvocal\\Main.java");
////        System.out.println(p.getName(p.getNameCount() - 2));
//
//        String json = """
//                {\s
//                   "name": "changeRecord",
//                   "version": "5.2.3.0",
//                   "dependencies":\s
//                     [
//                       {
//                         "name": "ECTR",
//                         "version": "5.2.3.0"
//                        }
//                     ],
//                   "backend_dependencies":
//                     [
//                       {
//                         "name": "S4CORE",
//                         "release": ">102",
//                		 "patch": ">0002"
//                        }
//                     ]
//                }\s
//                """;
//
//        ObjectMapper mapper = new ObjectMapper();
//        ModuleInfo moduleInfo = mapper.readValue(json, ModuleInfo.class);
//        System.out.println(moduleInfo);

        FlatDarkLaf.setup();
        ECTRServiceMock service = new ECTRServiceMock();
        Infra.initInstance(service);
        MainUI ui = new MainUI(service);

    }

    private static class ECTRServiceMock implements ECTRService
    {

        @Override
        public PlmLogger getPlmLogger()
        {
            return new PlmLogger()
            {
                @Override
                public void trace(String s)
                {
                    System.out.println(s);
                }

                @Override
                public void debug(String s)
                {
                    System.out.println(s);
                }

                @Override
                public void verbose(String s)
                {
                    System.out.println(s);
                }

                @Override
                public void warning(String s)
                {
                    System.err.println(s);
                }

                @Override
                public void error(String s)
                {
                    System.err.println(s);
                }

                @Override
                public void error(Throwable throwable)
                {
                    System.err.println(throwable.getMessage());
                }

                @Override
                public boolean isDebug()
                {
                    return true;
                }

                @Override
                public boolean isVerbose()
                {
                    return true;
                }
            };
        }

        @Override
        public RfcExecutor getRfcExecutor()
        {
            return null;
        }

        @Override
        public PlmDictionary getDictionary()
        {
            return null;
        }

        @Override
        public PlmEnvironment getEnvironment()
        {
            return new PlmEnvironment()
            {
                @Override
                public Map<String, String> getScriptEnvironment()
                {
                    return Map.of("system.start.instdir", Paths.get("c:", "users", "benda", "documents", "test", "addon-tool").toAbsolutePath().toString());
                }
            };
        }

        @Override
        public PlmLogonData getLogonData()
        {
            return null;
        }

        @Override
        public PlmStatusLine getStatusLine()
        {
            return new PlmStatusLine()
            {
                @Override
                public void setText(PlmStatusLineMode plmStatusLineMode, String s)
                {
                    PlmLogger logger = getPlmLogger();
                    switch (plmStatusLineMode)
                    {
                        case PLAIN -> logger.trace(s);
                        case SUCCESS -> logger.trace("SUCCESS: " + s);
                        case INFO -> logger.trace("INFO: " + s);
                        case WARNING -> logger.debug("WARNING: " + s);
                        case ERROR -> logger.error("ERROR: " + s);
                    }
                }

                @Override
                public void clearStatusLine()
                {

                }
            };
        }

        @Override
        public PlmPreferences getPlmPreferences()
        {
            return new PlmPreferences()
            {
                @Override
                public long longValue(String s, String s1, String... strings)
                {
                    return 0;
                }

                @Override
                public boolean booleanValue(String s, String s1, String... strings)
                {
                    return false;
                }

                @Override
                public double doubleValue(String s, String s1, String... strings)
                {
                    return 0;
                }

                @Override
                public String stringValue(String s, String s1, String... strings)
                {
                    return "";
                }

                @Override
                public List<String> listValue(String s, String s1, String... strings)
                {
                    return List.of();
                }

                @Override
                public Font fontValue(String s, String s1, String... strings)
                {
                    return null;
                }

                @Override
                public Color colorValue(String s, String s1, String... strings)
                {
                    return null;
                }

                @Override
                public Dimension dimensionValue(String s, String s1, String... strings)
                {
                    return null;
                }

                @Override
                public PlmPreference getPreference(String s, String s1, String... strings)
                {
                    return null;
                }
            };
        }

        @Override
        public ResourceAccessor getResourceAccessor()
        {
            return new ResourceAccessor()
            {

                @Override
                public ImageIcon getImageIcon(String s, IconSize iconSize)
                {
                    return switch (s)
                    {
                        case "search" ->
                        {
                            var img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                            char magnifiyingGlass = '\u2315';

                            Graphics graphics = img.getGraphics();
                            Font font = new JTextField().getFont().deriveFont(16f);
                            graphics.setFont(font);
                            graphics.drawString(String.valueOf(magnifiyingGlass), 5, 2 + font.getSize());
                            yield new ImageIcon(img);
                        }
                        case "addons.changeRecord" -> {
                            BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

                            try
                            {
                                var image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/addons.change-record.png")));
                                var scaled = image.getScaledInstance(32,32, Image.SCALE_SMOOTH);
                                yield  new ImageIcon(scaled);
                            }
                            catch (IOException e)
                            {
                                getPlmLogger().error(e.getMessage());
                                yield null;
                            }
                        }
                        default -> null;
                    };
                }

                @Override
                public Image getImage(String s)
                {
                    return null;
                }
            };
        }

        @Override
        public PlmCacheController getPlmCacheController()
        {
            return null;
        }

        @Override
        public ThumbnailAccessor getThumbnailAccessor()
        {
            return null;
        }
    }
}
