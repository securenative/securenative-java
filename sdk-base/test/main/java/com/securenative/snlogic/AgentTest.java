package com.securenative.snlogic;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.securenative.snlogic.Agent.*;

public class AgentTest {

    @BeforeClass
    public static void setup(){

    }

    @Test
    public void testPomPath(){
        String pomXmlPaths = getPomXmlPaths();
        Assert.assertTrue(pomXmlPaths.equals("debugger-agent-storage.jar"));
        System.out.println(pomXmlPaths);
    }

    @Test
    public void changeClassTest(){
        changeClassMethod(TestClassForByteBuddy.class, TestClassForByteBuddy2.class, "returnOne");
        TestClassForByteBuddy testClassForByteBuddy = new TestClassForByteBuddy();
        Assert.assertTrue(testClassForByteBuddy.returnOne().equals("2"));
    }

    @Test
    public void test() throws IOException {
        Assert.assertTrue(readVersionInfoInManifest().equals("Package name: Apache Log4j API, Package version: 2.11.2"));
    }

}
