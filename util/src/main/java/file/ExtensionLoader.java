/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author dpf
 * @param <T>
 */
public class ExtensionLoader<T> {

    private final String jar_path;
    private final String class_name;
    private final Class[] parameters;

    public ExtensionLoader(String jar_path, String class_name, Class... contructor_parameters) {
        this.jar_path = jar_path;
        this.class_name = class_name;
        this.parameters = contructor_parameters;
    }

    public T newInstance(Object... args) {
        try {
            URL u = new URL("file:" + jar_path);
            URL[] classLoaderUrls = {u};
            URLClassLoader loader = new URLClassLoader(classLoaderUrls, getClass().getClassLoader());
            return (T) Class.forName(class_name, true, loader).getConstructor(parameters).newInstance(args);
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public Object invokeStaticMethod(String method, Class[] parameters, Object... args){
        try {
            URL u = new URL("file:" + jar_path);
            URL[] classLoaderUrls = {u};
            URLClassLoader loader = new URLClassLoader(classLoaderUrls, getClass().getClassLoader());
            return Class.forName(class_name, true, loader).getMethod(method, parameters).invoke(null, args);
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String[] loadClasses(String pathToJar) {
        List<String> classes = new LinkedList<>();
        try {
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                classes.add(className);
            }
        } catch (IOException e) {

        }
        return classes.toArray(new String[classes.size()]);
    }

}
