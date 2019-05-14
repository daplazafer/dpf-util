package language;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import util.Logger;

/**
 *
 * @author Daniel Plaza
 */
public class Translator extends Logger{
    
    private static final List<Translatable> translatableObjects= new LinkedList<>();
    private static Language language;   

    public Translator() {
        super(null);
    }
    
    public Translator(String configLogger) {
        super(new File(configLogger));
    }
    
    public Translator(File configLogger) {
        super(configLogger);
    } 
    
    public void addComponent(Translatable translatableObject){
        log("Adding <"+translatableObject.textMarkup+"> markup to a component");
        translatableObjects.add(translatableObject);
    }
    
    public void addComponents(List<Translatable> translatableObjects){
        translatableObjects.stream().forEach((t) -> {
            addComponent(t);
        });
    }

    public Language getLanguage() {
        return language;
    }

    public void translateTo(Language language){
        Translator.language = language;
        translate();
    }

    private void translate(){
        translatableObjects.stream().forEach((Translatable t) -> {
            t.translate();
            log("Translating with the markup <"+t.textMarkup+">");
        });
    }
    
    public static class Translatable{
        private final Object component;
        private final String changeTextMethod;
        private final String textMarkup;

        public Translatable(Object component, String changeTextMethod, String textMarkup) {
            this.component = component;
            this.changeTextMethod = changeTextMethod;
            this.textMarkup = textMarkup;
            translate();
        }
        
        private void translate(){
            java.lang.reflect.Method method;
            try {
                method = component.getClass().getMethod(changeTextMethod,String.class);
                method.invoke(component, language.getText(textMarkup));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace(System.err);
            } catch (NullPointerException ex){
                //Do nothing, the language hasnt been defined yet
            } 
        }
        
    }

}
