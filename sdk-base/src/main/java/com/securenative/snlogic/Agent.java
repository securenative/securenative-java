package com.securenative.snlogic;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.apache.logging.log4j.util.ProcessIdUtil;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;


import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Agent {


    public static String getPomXmlPaths() {
        Reflections reflections = new Reflections(new ResourcesScanner());
        Set<String> resources = reflections.getResources(Pattern.compile(".*jar"));
        return resources.toString();
    }

    public static void changeClassMethod(Class cls, Class cls1, String methodName) {
        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(cls)
                .method(named(methodName)
                        .and(isDeclaredBy(cls)
                                .and(returns(String.class))))
                .intercept(MethodDelegation.to(cls1))
                .make()
                .load(cls.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();
    }

    public static Set<String> scanAllClasses() {
        Field f;
        Set<String> locations = new HashSet<>();
        try {
            f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            List<Class> classes = Collections.unmodifiableList((Vector<Class>) f.get(classLoader));
            for (Class next :classes){
                java.net.URL location = next.getResource('/' + next.getName().replace('.',
                        '/') + ".class");
                if (location != null && !Utils.isNullOrEmpty(location.getPath())){
                    locations.add(location.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;

    }

    public static String readVersionInfoInManifest() {
        Set<String> locations = new HashSet<>();
        //build an object whose class is in the target jar
        ProcessIdUtil object = new ProcessIdUtil();
        //navigate from its class object to a package object
        Package objPackage = object.getClass().getPackage();
        //examine the package object
        String name = objPackage.getSpecificationTitle();
        String version = objPackage.getSpecificationVersion();
        //some jars may use 'Implementation Version' entries in the manifest instead
        return "Package name: " + name + ", Package version: " + version;
    }
}
