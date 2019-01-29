package com.tests;

import org.junit.Assert;

import java.io.*;

// Utility methods for all unit test classes
public class TestUtil {

    // For obtaining class loader
    private static TestUtil util = new TestUtil();

    // Reads test resource from disk
    public static String readTestResource(String name) {
        ClassLoader classLoader = util.getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\r\n");
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            Assert.assertTrue(false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Assert.assertTrue(false);
        }
        return sb.toString();
    }

    // Private constructor, this class can never be instantiated
    private TestUtil() {
    }
}
