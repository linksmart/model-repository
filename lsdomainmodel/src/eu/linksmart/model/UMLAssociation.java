package eu.linksmart.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by rachev on 20.10.2015.
 */
public class UMLAssociation {

    public String umlID;
    public String stereotypeID;
    public String stereotypeName;
    public String fromClassID;
    public String toClassID;
    public UMLModel umlModel;

    public UMLAssociation(Node node, UMLModel umlModel)throws XPathExpressionException {
        this.umlModel = umlModel;
/*
-<ownedMember name="Assoc" xmi:id="rS7woQqECGlCdgUR" xmi:type="uml:Association" isLeaf="false" isAbstract="false" isDerived="false">
    <memberEnd xmi:idref="wy7woQqECGlCdgUT"/>
    -<ownedEnd name="" xmi:id="wy7woQqECGlCdgUT" xmi:type="uml:Property" type="osl_m3KECGlCdh4B" isLeaf="false" isStatic="false" isReadOnly="false" isDerivedUnion="false" isDerived="false" aggregation="none" isNavigable="true" association="rS7woQqECGlCdgUR">
        -<xmi:Extension extender="Visual Paradigm">
            <associationEnd/>
            -<qualifier name="" xmi:id="Sy7woQqECGlCdgUV" xmi:type="qualifier">
                -<xmi:Extension extender="Visual Paradigm">
                    <qualityScore value="-1"/>
                </xmi:Extension>
            </qualifier>
            <qualityScore value="-1"/>
        </xmi:Extension>
    </ownedEnd>
    <memberEnd xmi:idref="Sy7woQqECGlCdgUW"/>
    -<ownedEnd xmi:id="Sy7woQqECGlCdgUW" xmi:type="uml:Property" type="rFd_m3KECGlCdh4N" isLeaf="false" isStatic="false" isReadOnly="false" isDerivedUnion="false" isDerived="false" aggregation="none" isNavigable="true" association="rS7woQqECGlCdgUR">
        -<xmi:Extension extender="Visual Paradigm">
            <associationEnd/>
            -<qualifier name="" xmi:id="Sy7woQqECGlCdgUX" xmi:type="qualifier">
                -<xmi:Extension extender="Visual Paradigm">
                    <qualityScore value="-1"/>
                </xmi:Extension>
            </qualifier>
            <qualityScore value="-1"/>
        </xmi:Extension>
    </ownedEnd>
    -<xmi:Extension extender="Visual Paradigm">
        <qualityScore value="-1"/>
        <appliedStereotype xmi:value="Association_LS_association_id"/>
    </xmi:Extension>
</ownedMember>
*/

        if (node instanceof Element) {
            Element element = ((Element) node);
            umlID = element.getAttribute("xmi:id");

            // Stereotype_ID - works only with one stereotype per class
            NodeList nList = element.getElementsByTagName("appliedStereotype");
            if (nList.getLength()>0)
                stereotypeID = ((Element)nList.item(0)).getAttribute("xmi:value");

//            System.out.println("UMLAssociation        umlID: " + umlID);
//            System.out.println("UMLAssociation stereotypeID: " + stereotypeID);
        }

        // Process the association ends
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "//ownedEnd"; // get all definitions
        NodeList nList = (NodeList) xPath.compile(expression).evaluate( node, XPathConstants.NODESET);
        if (nList.getLength()>1){

            Element element = (Element)nList.item(0);
            String classID = element.getAttribute( "type");

            if (umlModel.umlClasses.containsKey(classID) && umlModel.umlClasses.get(classID).isDevice())
                fromClassID = classID;
            else
                toClassID = classID;

            element = (Element)nList.item(1);
            classID = element.getAttribute( "type");

            if (umlModel.umlClasses.containsKey(classID) && umlModel.umlClasses.get(classID).isDevice())
                fromClassID = classID;
            else
                toClassID = classID;

//            System.out.println("UMLAssociation  fromClassID: " + fromClassID);
//            System.out.println("UMLAssociation    toClassID: " + toClassID);
        }
    }

    public String getUmlID(){
        return umlID;
    }

    public boolean isLSArtifact(){
        // return if stereotypeID is a LS stereotype name
        return stereotypeID!=null && stereotypeID.equals(umlModel.stereotypeLsAssociationID);
    }
}
