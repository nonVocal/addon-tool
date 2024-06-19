package dev.nonvocal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nonvocal.addon.module.ModuleInfo;

public class Main
{
    public static void main(String[] args) throws JsonProcessingException
    {
//        Path p = Paths.get("C:\\Users\\benda\\IdeaProjects\\addon-tool\\src\\main\\java\\dev\\nonvocal\\Main.java");
//        System.out.println(p.getName(p.getNameCount() - 2));

        String json = """
                {\s
                   "name": "changeRecord",
                   "version": "5.2.3.0",
                   "dependencies":\s
                     [
                       {
                         "name": "ECTR",
                         "version": "5.2.3.0"
                        }
                     ],
                   "backend_dependencies":
                     [
                       {
                         "name": "S4CORE",
                         "release": ">102",
                		 "patch": ">0002"
                        }
                     ]
                }\s
                """;

        ObjectMapper mapper = new ObjectMapper();
        ModuleInfo moduleInfo = mapper.readValue(json, ModuleInfo.class);
        System.out.println(moduleInfo);

    }
}
