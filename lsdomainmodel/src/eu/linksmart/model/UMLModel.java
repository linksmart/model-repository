package eu.linksmart.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import javax.json;

/**
 * Created by rachev on 21.10.2015.
 */
public class UMLModel {



    private String name;

    private String version;

    // Class Definitions
    public Map<String,UMLClass> umlClasses = new HashMap<String, UMLClass>();

    // Instance Definitions
    public Map<String,UMLInstance> umlInstances = new HashMap<String, UMLInstance>();

    // Stereotypes
    public final static String STEREOTYPE_DEVICE_NAME = "Device";
    public final static String STEREOTYPE_RESOURCE_NAME = "Resource";
    public final static String STEREOTYPE_LS_ASSOCIATION_NAME = "LS_association";
    public String stereotypeDeviceID;
    public String stereotypeResourceID;
    public String stereotypeLsAssociationID;
    /*
        -<ownedMember name="LS_association" xmi:id="Association_LS_association_id" xmi:type="uml:Stereotype">
            -<xmi:Extension extender="Visual Paradigm">
                <baseType value="Association"/>
                <qualityScore value="-1"/>
            </xmi:Extension>
        </ownedMember>

        -<ownedMember name="Device" xmi:id="Class_Device_id" xmi:type="uml:Stereotype">
            -<xmi:Extension extender="Visual Paradigm">
                <baseType value="Class"/>
                <qualityScore value="-1"/>
            </xmi:Extension>
        </ownedMember>
    */

    // Association Definitions
    public Map<String,UMLAssociation> umlAssociations= new HashMap<String, UMLAssociation>();

    // Link Definitions
    public Map<String, UMLLink> umlLinks = new HashMap<String, UMLLink>();

    public Set<UMLInstance> getTopInstances(){

        Set<UMLInstance> instances = new HashSet<UMLInstance>();
        for(Entry e : umlInstances.entrySet()){
            UMLInstance inst = (UMLInstance)e.getValue();
            if( !inst.hasUpper())
                instances.add( inst);
        }
        return instances;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private void extractStereotypeDefinitions(NodeList nList ) throws XPathExpressionException {

/*		<ownedMember name="Device" xmi:id="Class_Device_id" xmi:type="uml:Stereotype">
			<xmi:Extension extender="Visual Paradigm">
				<baseType value="Class"/>
				<qualityScore value="-1"/>
			</xmi:Extension>
		</ownedMember>
		<ownedMember name="Resource" xmi:id="Class_Resource_id" xmi:type="uml:Stereotype">
			<xmi:Extension extender="Visual Paradigm">
				<baseType value="Class"/>
				<qualityScore value="-1"/>
			</xmi:Extension>
		</ownedMember>
*/
//        System.out.println("\n ---------------------------- extracting Stereotype Definitions");
        for (int n = 0; n < nList.getLength(); n++) {
            Node node = nList.item(n);
            if (node instanceof Element) {
                Element element = ((Element) node);
                String name = element.getAttribute("name");
                String stereotypeID = element.getAttribute("xmi:id");

//                System.out.println("Stereotype            name: " + name);
//                System.out.println("Stereotype              ID: " + stereotypeID);

                if (name.equals(STEREOTYPE_DEVICE_NAME)){
                    stereotypeDeviceID = stereotypeID;
                } else if (name.equals(STEREOTYPE_RESOURCE_NAME)){
                    stereotypeResourceID = stereotypeID;
                } else if (name.equals(STEREOTYPE_LS_ASSOCIATION_NAME)){
                    stereotypeLsAssociationID = stereotypeID;
                }
            }
//            System.out.println(" \n Stereotype: " + n );
        }
    }

    private void extractClassDefinitions(NodeList nList )throws XPathExpressionException {

//        System.out.println("\n ---------------------------- extracting Class Definitions");
        for (int n = 0; n < nList.getLength(); n++) {
            Node nNode = nList.item(n);

//            System.out.println(" \n Class: " + n);
            UMLClass umlClass = new UMLClass( nNode, this);
            if (umlClass.isLSArtifact())
                umlClasses.put(umlClass.getUmlID(), umlClass);
        }
    }

    private void extractAssociationDefinitions(NodeList nList ) throws XPathExpressionException {
//        System.out.println("\n ---------------------------- extracting Association Definitions");
        for (int n = 0; n < nList.getLength(); n++) {
            Node nNode = nList.item(n);

//            System.out.println(" \n Association: " + n + " : " + nNode.getNodeName());
            UMLAssociation umlAssociation = new UMLAssociation( nNode, this);
            if (umlAssociation.isLSArtifact())
                umlAssociations.put(umlAssociation.getUmlID(), umlAssociation);
        }
    }

    private void extractInstanceSpecifications(NodeList nList ) throws XPathExpressionException {
//        System.out.println("\n ---------------------------- extract Instance Specifications");
/*
        -<ownedMember xmi:id="Bl3NkQqECGlCdgZ5" xmi:type="uml:InstanceSpecification">
            <classifier xmi:idref="rS7woQqECGlCdgUR"/>
            +<xmi:Extension extender="Visual Paradigm">
*/
        for (int n = 0; n < nList.getLength(); n++) {
            Node nNode = nList.item(n);
            Node classifierNode = XMLTools.getNode( "classifier", nNode.getChildNodes());
            if (classifierNode instanceof Element) {
                String classID = ((Element) classifierNode).getAttribute("xmi:idref");
//                System.out.println("\n classID of an instance: " + classID);
                if ( isUMLClass( classID)){
//                    System.out.println("\n --------------------- create an instance");
                    UMLInstance umlInstance = new UMLInstance( nNode, this);
                    if ( umlInstance.isLSArtifact())
                        umlInstances.put(umlInstance.getUmlID(), umlInstance);
                } else if (isUMLAssociation( classID)) {
//                    System.out.println("\n --------------------- create a link");
                    UMLLink umlLink = new UMLLink( nNode, this);
                    // consider only tagged links
                    if ( umlLink.isLSArtifact())
                        umlLinks.put(umlLink.getUmlID(), umlLink);
                }
            }
        }
     }

    private void propagateLinkInfo(){
        for (UMLLink link : umlLinks.values()){
            UMLInstance fromInstance = umlInstances.get( link.fromInstanceID);
            UMLInstance toInstance = umlInstances.get( link.toInstanceID);
            // a link should from Device to Resource
            if ( fromInstance.getStereotypeName().equals( STEREOTYPE_DEVICE_NAME)
                    && toInstance.getStereotypeName().equals( STEREOTYPE_RESOURCE_NAME)){
                toInstance.upperInstance = fromInstance;
                fromInstance.descendingInstances.add(toInstance);
            } else if ( fromInstance.getStereotypeName().equals( STEREOTYPE_RESOURCE_NAME)
                    && toInstance.getStereotypeName().equals( STEREOTYPE_DEVICE_NAME)){
                // change the link direction
                fromInstance.upperInstance = toInstance;
                toInstance.descendingInstances.add(fromInstance);
            }
        }
    }

    public boolean isUMLClass(String umlID){
        return umlClasses.containsKey( umlID);
    }

    public boolean isUMLAssociation(String umlID){
        return umlAssociations.containsKey( umlID);
    }

    public void parseDom( Document doc ) throws XPathExpressionException {
        doc.getDocumentElement().normalize();

        // Using XPath to locate the class definitions
        NodeList nList;
        String expression;
        XPath xPath =  XPathFactory.newInstance().newXPath();

        expression = "//ownedMember[@type='uml:Stereotype']"; // get all Stereotype definitions
        nList = (NodeList) xPath.compile(expression).evaluate( doc, XPathConstants.NODESET);
        extractStereotypeDefinitions( nList);

        expression = "//ownedMember[@type='uml:Class']"; // get all class definitions
        nList = (NodeList) xPath.compile(expression).evaluate( doc, XPathConstants.NODESET);
        extractClassDefinitions( nList);

        expression = "//ownedMember[@type='uml:Association']"; // get all Association  definitions
        nList = (NodeList) xPath.compile(expression).evaluate( doc, XPathConstants.NODESET);
        extractAssociationDefinitions(nList);

        expression = "//ownedMember[@type='uml:InstanceSpecification']"; // get all InstanceSpecification definitions
        nList = (NodeList) xPath.compile(expression).evaluate( doc, XPathConstants.NODESET);
        extractInstanceSpecifications(nList);

        propagateLinkInfo();

//        System.out.println( toStringJSON());
    }

    public JSONObject toJSON(){

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("name", name);
        jsonObj.put("version", version);

        // Instance Tree
        JSONArray jsonArray = new JSONArray();
        Set<UMLInstance> instances = getTopInstances();
        for( UMLInstance instance : instances) {
            jsonArray.put(instance.toJSON());
        }
        jsonObj.put("model", jsonArray);

        return jsonObj;
    }

    public String toStringJSON(){
        return toJSON().toString( 3);
    }

    public String getStereotypeName( String stereotypeID){
        String name = stereotypeID;
        if (stereotypeID.equals( stereotypeDeviceID))
            name = UMLModel.STEREOTYPE_DEVICE_NAME;
        else if (stereotypeID.equals( stereotypeResourceID))
            name = UMLModel.STEREOTYPE_RESOURCE_NAME;
        else if (stereotypeID.equals( stereotypeLsAssociationID))
            name = UMLModel.STEREOTYPE_LS_ASSOCIATION_NAME;

        return name;
    }
}
