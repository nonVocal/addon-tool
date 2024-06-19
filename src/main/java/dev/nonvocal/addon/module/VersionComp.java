package dev.nonvocal.addon.module;

public record VersionComp(String operator, String version, String raw)
{
    public static VersionComp of(String versionString)
    {
        char[] chars = versionString.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (Character.isDigit(chars[i]))
            {
                if (i == 0)
                    return new VersionComp("=", versionString, versionString);
                else
                    return new VersionComp(versionString.substring(0, i), versionString.substring(i), versionString);
            }
        }

        return new VersionComp("", "", "");
    }
}
