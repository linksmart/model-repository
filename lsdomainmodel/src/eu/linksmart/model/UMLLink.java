package eu.linksmart.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by rachev.rossen on 28.10.2015.
 */
public class UMLLink {

    public String umlID;
    private UMLAssociation type;
    public String typeClassID;
    public String stereotypeID;
    public String fromInstanceID;
    public String toInstanceID;

    public UMLLink(Node node, UMLModel umlModel){
/*		<ownedMember xmi:id="Bl3NkQqECGlCdgZ5" xmi:type="uml:InstanceSpecification">
			<classifier xmi:idref="rS7woQqECGlCdgUR"/>
			<xmi:Extension extender="Visual Paradigm">
				<modelType value="Link"/>
				<from idref="0BA2W3KECGlCdh7T"/>
				<to idref="h2Q2W3KECGlCdh7h"/>
				<qualityScore value="-1"/>
			</xmi:Extension>
		</ownedMember>
*/
        if (node instanceof Element) {
            Element element = ((Element) node);
            umlID = element.getAttribute("xmi:id");
            String name = element.getAttribute("name");

            // ClassID of the Association
            NodeList nList = element.getElementsByTagName("classifier");
            if (nList.getLength() > 0)
                typeClassID = ((Element) nList.item(0)).getAttribute("xmi:idref");

            if (umlModel.umlAssociations.containsKey(typeClassID)){
                type = umlModel.umlAssociations.get(typeClassID);
                stereotypeID =  type.stereotypeID;
            }

            // From
            nList = element.getElementsByTagName("from");
            if (nList.getLength() > 0)
                fromInstanceID = ((Element) nList.item(0)).getAttribute("idref");

            // To
            nList = element.getElementsByTagName("to");
            if (nList.getLength() > 0)
                toInstanceID = ((Element) nList.item(0)).getAttribute("idref");

//            System.out.println("UMLLink          umlID: " + umlID);
//            System.out.println("UMLLink           name: " + name);
//            System.out.println("UMLLink    typeClassID: " + typeClassID);
//            System.out.println("UMLLink fromInstanceID: " + fromInstanceID);
//            System.out.println("UMLLink   toInstanceID: " + toInstanceID);
        }

    }

    public String getUmlID(){
        return umlID;
    }

    public boolean isLSArtifact(){
        // return if stereotypeID is a LS stereotype name
        return type.isLSArtifact();
    }
}
