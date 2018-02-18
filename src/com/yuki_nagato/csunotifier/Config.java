package com.yuki_nagato.csunotifier;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Config {
    public final int interval;
    public final String recordFile;
    public final List<Map<String, String>> students;
    public final Map<String, String> email;

    public Config(File yamlFile) throws IOException {
        YamlReader reader;
        try {
            reader = new YamlReader(new InputStreamReader(new FileInputStream(yamlFile),"utf-8"));
        }
        catch (FileNotFoundException e) {
            System.err.println("配置文件 "+yamlFile.getPath()+" 未找到");
            throw e;
        }
        try {
            Map<String, Object> map = (Map)reader.read();
            reader.close();
            interval = Integer.parseInt((String)map.get("interval"));
            recordFile = (String)map.get("record-file");
            students = (List)map.get("students");
            email = (Map)map.get("email");
        }
        catch (YamlException | ClassCastException e) {
            System.err.println("配置文件 "+yamlFile.getPath()+" 格式错误");
            throw e;
        }
    }
}
