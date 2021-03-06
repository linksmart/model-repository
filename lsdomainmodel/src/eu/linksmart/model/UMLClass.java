package eu.linksmart.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rachev on 20.10.2015.
 */
public class UMLClass {
    private UMLModel umlModel;
    public String umlID;
    public String className;
    public String stereotypeID;
    public Map<String, String> attributes = new HashMap<String, String>();
//    public UMLClass parent = null; //inheritance, not used currently

    public UMLClass(Node node, UMLModel model){
        this.umlModel = model;

// 		<ownedMember isAbstract="false" isActive="false" isLeaf="false" name="Class_1" visibility="public" xmi:id="osl_m3KECGlCdh4B" xmi:type="uml:Class">
//			<xmi:Extension extender="Visual Paradigm">
//				<isRoot xmi:value="false"/>
//				<modelType value="Class"/>
//				<businessModel xmi:value="false"/>
//				<qualityScore value="-1"/>
//				<appliedStereotype xmi:value="Class_Device_id"/>
//			</xmi:Extension>
//			<ownedAttribute aggregation="none" isDerived="false" isDerivedUnion="false" isID="false" isLeaf="false" isReadOnly="false" isStatic="false" name="id" type="string_id" visibility="public" xmi:id="4gPekQqECGlCdgU5" xmi:type="uml:Property">
//				<xmi:Extension extender="Visual Paradigm">
//					<attribute/>
//					<isVisble xmi:value="true"/>
//					<qualityScore value="-1"/>
//				</xmi:Extension>
//			</ownedAttribute>
//			<ownedAttribute aggregation="none" isDerived="false" isDerivedUnion="false" isID="false" isLeaf="false" isReadOnly="false" isStatic="false" name="ls_id" type="string_id" visibility="public" xmi:id="UR2.kQqECGlCdgWa" xmi:type="uml:Property">
//				<xmi:Extension extender="Visual Paradigm">
//					<attribute/>
//					<isVisble xmi:value="true"/>
//					<qualityScore value="-1"/>
//				</xmi:Extension>
//			</ownedAttribute>
//		</ownedMember>

        if (node instanceof Element) {
            Element element = ((Element) node);
            umlID = element.getAttribute("xmi:id");
            className = element.getAttribute("name");

            // Stereotype_ID - works only with one stereotype per class
            NodeList nList = element.getElementsByTagName("appliedStereotype");
            if (nList.getLength()>0)
                stereotypeID = ((Element)nList.item(0)).getAttribute("xmi:value");

//            System.out.println("UMLClass        umlID: " + umlID);
//            System.out.println("UMLClass    className: " + className);
//            System.out.println("UMLClass stereotypeID: " + stereotypeID);

            // Attributes
            nList = element.getElementsByTagName("ownedAttribute");
            for (int item = 0; item < nList.getLength(); item++) {
                Node nNode = nList.item(item);
                String attrName = ((Element)nNode).getAttribute( "name");
                String attrID = ((Element)nNode).getAttribute( "xmi:id");
                attributes.put(attrID, attrName);
//                System.out.println("UMLClass   attribute[" + item + "]: " + attrName);
//                System.out.println("UMLClass attributeID[" + item + "]: " + attrID);
            }

        }
    }

    public String getUmlID(){
        return umlID;
    }

    public boolean isLSArtifact(){
        return isDevice() || isRecourse();
    }

    public boolean isDevice(){
        return stereotypeID != null && stereotypeID.equals(umlModel.stereotypeDeviceID);
    }

    public boolean isRecourse(){
        return stereotypeID != null && stereotypeID.equals(umlModel.stereotypeResourceID);
    }

    public String getStereotypeName(){
        return umlModel.getStereotypeName( stereotypeID);
    }
}
