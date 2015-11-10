package eu.linksmart.model;

import org.w3c.dom.*;

import java.io.PrintStream;

/**
 * Created by rachev on 03.11.2015.
 */
public final class XMLTools {
    private XMLTools(){
    }

    public static void printlnCommon( Node n) {
        PrintStream out = System.out;
        out.print(" nodeName=\"" + n.getNodeName() + "\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            out.print(" uri=\"" + val + "\"");
        }

        val = n.getPrefix();

        if (val != null) {
            out.print(" pre=\"" + val + "\"");
        }

        val = n.getLocalName();
        if (val != null) {
            out.print(" local=\"" + val + "\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            out.print(" nodeValue=");
            if (val.trim().equals("")) {
                // Whitespace
                out.print("[WS]");
            }
            else {
                out.print("\"" + n.getNodeValue() + "\"");
            }
        }

//        val = n.getTextContent();
//        if (val != null) {
//            out.print(" nodeTextContent=");
//            if (val.trim().equals("")) {
//                // Whitespace
//                out.print("[WS]");
//            }
//            else {
//                out.print("\"" + n.getTextContent() + "\"");
//            }
//        }

        out.println();

        if (n.hasAttributes()){
            listAllAttributes( (Element) n);
        }
    }

    public static void printlnSubtree( Node n, int level) {
        System.out.println("Level "+ level+" ----------------------------");
        printlnCommon( n);
        NodeList nList = n.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            printlnSubtree(nNode, level++);
        }

    }


    public static void listAllAttributes(Element element) {

        System.out.println("List attributes for node: " + element.getNodeName());

        // get a map containing the attributes of this node
        NamedNodeMap attributes = element.getAttributes();

        // get the number of nodes in this map
        int numAttrs = attributes.getLength();

        for (int i = 0; i < numAttrs; i++) {
            Attr attr = (Attr) attributes.item(i);

            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();

            System.out.println("Found (attribute : value) - " + attrName + " : " + attrValue);

        }
    }

    public static Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }
        return null;
    }

}
