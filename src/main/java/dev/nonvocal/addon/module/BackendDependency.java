package dev.nonvocal.addon.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackendDependency(String name, VersionComp release, VersionComp patch)
{
    public BackendDependency(@JsonProperty("name") String name, @JsonProperty("release") String release,
                      @JsonProperty("patch") String patch)
    {
        this(name, VersionComp.of(release), VersionComp.of(patch));
    }
}
