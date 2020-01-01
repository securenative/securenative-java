package com.securenative.packagemanager;

import com.securenative.utils.SnUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PackageManager {
    private static Document readPackageFile(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            // TODO add logging
        }

        return document;
    }

    private static Dependency[] parseDependencies(NodeList nodeList) {
        Dependency[] dependencies = new Dependency[nodeList.getLength()];

        int j = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                dependencies[j] = new Dependency(eElement.getAttribute("groupId"), eElement.getAttribute("artifactId"), eElement.getAttribute("version"));
                j += 1;
            }
        }

        return dependencies;
    }

    private static String parseParent(NodeList nodeList, String key) {
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) node;
            return eElement.getAttribute(key);
        }

        return "";
    }

    public static SnPackage getPackage(String packageFilePath) {
        Document document = readPackageFile(packageFilePath);

        NodeList deps = document.getElementsByTagName("dependencies");
        NodeList parent = document.getElementsByTagName("parent");

        Dependency[] dependencies = parseDependencies(deps);
        String dependenciesHash = SnUtils.calculateHash(Arrays.toString(dependencies));
        String artifactId = parseParent(parent, "artifactId");
        String groupId = parseParent(parent, "groupId");
        String version = parseParent(parent, "version");

        return new SnPackage(artifactId, groupId, version, dependencies, dependenciesHash);
    }
}
