/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import file.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import util.Logger;

/**
 *
 * @author dpf
 */
public class Configuration extends Logger {
    
    private File configPath;
    private Properties p;
    private List<Setting> settings;

    public Configuration(File configPath, List<Setting> defaultSettings, File configLogger) {
        super(configLogger);
        this.configPath = configPath;
        this.settings=defaultSettings;
        p = new Properties();
        readConfig();
    }
    
    public Configuration(File configPath, List<Setting> settings){
        this(configPath, settings,null);
    }
    
    private void readConfig(){
        if(!configPath.exists() || configPath.length()==0){
            settings.stream().forEach((s) -> {
                p.setProperty(s.name, s.value);
            });
            log("Configuration file \""+configPath.getName()+"\" created");
            save(false);
        }else{
            InputStream input = null;
            try {
                input = new FileInputStream(configPath);
                p.load(input);
                log("Configuration file \""+configPath.getName()+"\" loaded");
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            }
        }
    }
    
    public void reloadConfiguration(){
        readConfig();
    }
    
    public void setSetting(Setting s) {
        if(p.setProperty(s.name, s.value) == null){
            log("Creating the setting \""+s.name + "\" with the value of \"" + s.value+"\"");
        }else{
            log("\""+s.name + "\" set to \"" + s.value+"\"");
        }
    }
    
    public List<String> getSettingNames(){
        List<String> sn = new ArrayList<>();
        Enumeration<String> e = (Enumeration<String>) p.propertyNames();
        while(e.hasMoreElements()){
            sn.add(e.nextElement());
        }
        return sn;
    }
    
    public String getSetting(String s){
        return p.getProperty(s);       
    }
    
    public static class Setting {
        private final String name,value;

        public Setting(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getvalue() {
            return value;
        }
        
    }
    
    /**
     * Save the settings to the configuration file
     * @param backup if true a backup file will be created
     */
    public void save(boolean backup) {
        FileOutputStream output;
        try {
            if(configPath.exists() && backup)
                FileUtil.copyFile(configPath.getName(),configPath + ".backup"); 
            output = new FileOutputStream(configPath);
            p.store(output, null);
            log("Configuration file \""+configPath.getName()+"\" saved");
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
            
    }
    
    /**
     * Save without backup
     */
    public void save(){
        save(false);
    }
    
}
