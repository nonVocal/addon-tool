package dev.nonvocal.addon.module;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class ModuleInfo
{
    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private String version;

    @JsonProperty("dependencies")
    private Collection<Dependency> dependencies;

    @JsonProperty("backend_dependencies")
    private Collection<BackendDependency> backendDependencies;

    public String name()
    {
        return name;
    }


    public String version()
    {
        return version;
    }


    public Collection<Dependency> dependencies()
    {
        return dependencies;
    }


    public Collection<BackendDependency> backendDependencies()
    {
        return backendDependencies;
    }
}
