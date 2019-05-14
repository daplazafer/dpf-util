/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import config.Configuration;
import config.Configuration.Setting;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import util.ConsoleIO;


/**
 *
 * @author dpf
 */
public class ConfigurationTest {
    
    public static final String COLOR = "color";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DAY = "day";
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configuration c;
        List<Setting> defaultSettings = new ArrayList<>();
        defaultSettings.add(new Setting(COLOR,"blue"));
        defaultSettings.add(new Setting(NUMBER,"12"));
        defaultSettings.add(new Setting(NAME,"my name"));
        defaultSettings.add(new Setting(TYPE,"java"));
        
        c = new Configuration(new File("test_configuration.cfg"),defaultSettings,new File("test_configuration.log"));
        
        c.setSetting(new Setting(COLOR,"green"));
        
        ConsoleIO.println("The "+COLOR+" is "+c.getSetting(COLOR));
        
        c.setSetting(new Setting(DAY,"saturday"));
        c.save(true);
        
        ConsoleIO.println("The "+COLOR+" is "+c.getSetting(COLOR));
        ConsoleIO.println("The "+DAY+" is "+c.getSetting(DAY));
        ConsoleIO.println("My "+NAME+" is "+c.getSetting(NAME));
        
        ConsoleIO.println("---------------------------------------------------------------------------");
        List<String> sn = c.getSettingNames();
        
        for(String s:sn){
            ConsoleIO.println(s);
        }
        
        //ConsoleIO.println(c.getSetting("asdf"));
        
    }    
    
}
