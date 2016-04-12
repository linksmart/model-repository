package eu.linksmart.services.mr;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rachev on 12/16/2015.
 */
public class UMLInstance {

    private static final Logger LOG = Logger.getLogger(UMLInstance.class);

    public String umlID;
    public String name;
    public String classID;
    public UMLClass type;
    public UMLInstance upperInstance = null;
    public Set<UMLInstance> descendingInstances = new HashSet<UMLInstance>(); // only wia an association with "LS_association" stereotype,
    public Map<String, String> ls_attrValues= new HashMap< String, String>();
    public Map<String, String> attrValues= new HashMap< String, String>(); // attrID, attrValue

    private final static String ATTR_NAME_META = "ls_meta";
    private final static String ATTR_NAME_TTL = "ls_ttl";
    private final static String ATTR_NAME_PROTOCOL_EXT = "ls_ext_protocol";
    private final static String ATTR_NAME_PROTOCOL_INT = "ls_int_protocol";
    private final static String ATTR_NAME_DESCRIPTION = "ls_description";
    private final static String ATTR_NAME_REPRESENTATION = "ls_representation";


    public UMLInstance getUpperInstance(){
        return upperInstance;
    }

    public boolean hasUpper(){
        return upperInstance != null;
    }

    public JSONObject toJSON(){
        JSONObject out = new JSONObject();
        out.put( "ls_id", ls_attrValues.get( "ls_id"));
        out.put( "ls_name", ls_attrValues.get( "ls_name"));
        out.put( "ls_stereotype", type.getStereotypeName());

        //out.put(ls_attrValues)
        JSONObject ls_attributes = new JSONObject();
        if( type.getStereotypeName().equalsIgnoreCase(UMLModel.STEREOTYPE_DEVICE_NAME)) {
            ls_attributes.put("description", ls_attrValues.get( ATTR_NAME_DESCRIPTION));
            if (ls_attrValues.containsKey( ATTR_NAME_META))
                ls_attributes.put("meta", new JSONObject( ls_attrValues.get( ATTR_NAME_META)));
            ls_attributes.put("ttl", Integer.parseInt(ls_attrValues.get( ATTR_NAME_TTL)));
        } else {  //UMLModel.STEREOTYPE_RESOURCE_NAME
            if (ls_attrValues.containsKey(ATTR_NAME_META))
                ls_attributes.put("meta", new JSONObject( ls_attrValues.get( ATTR_NAME_META)));
            if (ls_attrValues.containsKey( ATTR_NAME_REPRESENTATION))
                ls_attributes.put("representation", new JSONObject( ls_attrValues.get( ATTR_NAME_REPRESENTATION)));
            if (ls_attrValues.containsKey( ATTR_NAME_PROTOCOL_INT))
                ls_attributes.put("int_protocol", new JSONObject( completeProtocol( ls_attrValues.get(ATTR_NAME_PROTOCOL_INT))));
            if (ls_attrValues.containsKey( ATTR_NAME_PROTOCOL_EXT))
                ls_attributes.put("ext_protocol", new JSONObject( completeProtocol(ls_attrValues.get(ATTR_NAME_PROTOCOL_EXT))));
        }
        out.put("ls_attributes", ls_attributes);

        out.put( "domain_class", type.className);

        //out.put(domain_attributes)
        JSONObject domain_attributes = new JSONObject();
        for(Map.Entry e : attrValues.entrySet()){
            String attrValue = (String)e.getValue();
            String attrName = (String)e.getKey();
            domain_attributes.put( attrName, attrValue);
        }
        out.put( "domain_attributes", domain_attributes);

        //out.put( children);
        JSONArray children = new JSONArray();
        for( UMLInstance instance : descendingInstances) {
            children.put( instance.toJSON());
        }
        out.put("children", children);

        return out;
    }

    public UMLInstance(Node node, UMLModel umlModel){

/*		<ownedMember name="Instance 1" xmi:id="0BA2W3KECGlCdh7T" xmi:type="uml:InstanceSpecification">
			<slot definingFeature="4gPekQqECGlCdgU5" name="Slot" xmi:id="YvUFkQqECGlCdgY7" xmi:type="uml:Slot">
				<value body="d001" xmi:id="yyCFkQqECGlCdgY_" xmi:type="uml:OpaqueExpression"/>
				<xmi:Extension extender="Visual Paradigm">
					<qualityScore value="-1"/>
				</xmi:Extension>
			</slot>
			<slot definingFeature="UR2.kQqECGlCdgWa" name="Slot" xmi:id="z0MFkQqECGlCdgY8" xmi:type="uml:Slot">
				<value body="ls01" xmi:id="gfaFkQqECGlCdgZB" xmi:type="uml:OpaqueExpression"/>
				<xmi:Extension extender="Visual Paradigm">
					<qualityScore value="-1"/>
				</xmi:Extension>
			</slot>
			<classifier xmi:idref="osl_m3KECGlCdh4B"/>
			<xmi:Extension extender="Visual Paradigm">
				<qualityScore value="-1"/>
			</xmi:Extension>
		</ownedMember>
*/
        if (node instanceof Element) {
            Element element = ((Element) node);
            umlID = element.getAttribute("xmi:id");
            name = element.getAttribute("name");

            // Class
            NodeList nList = element.getElementsByTagName("classifier");
            if (nList.getLength()>0) {
                classID = ((Element) nList.item(0)).getAttribute("xmi:idref");
                type = umlModel.umlClasses.get(classID);
            }

            LOG.trace("UMLInstance          umlID: " + umlID);
            LOG.trace("UMLInstance           name: " + name);
            LOG.trace("UMLInstance        classID: " + classID);

            // Attributes
            nList = element.getElementsByTagName("slot");
            for (int item = 0; item < nList.getLength(); item++) {
                Node nNode = nList.item(item);
                String attrID = ((Element)nNode).getAttribute( "definingFeature");
                String attrName = type.getAttributeName( attrID);

                //value
                String attrValue = null;
                NodeList subNodes = ((Element)nNode).getElementsByTagName("value");
                if (subNodes.getLength()>0){
                    attrValue = ((Element)subNodes.item(0)).getAttribute("body");
                    if( attrName.toLowerCase().startsWith("ls_"))
                        ls_attrValues.put( attrName, attrValue);
                    else
                        attrValues.put( attrName, attrValue);
                }

                LOG.trace("UMLInstance attributeID[" + item + "]: " + attrID);
                LOG.trace("UMLInstance   attrValue[" + item + "]: " + attrValue);
            }
        }
    }

    public String getUmlID(){
        return umlID;
    }

    public boolean isLSArtifact(){
        // return if stereotypeID is a LS stereotype name
        return type.isLSArtifact();
    }

    public String getStereotypeName(){
        return type.getStereotypeName();
    }

    private String completeProtocol( String jsonString){
        String trimmed = jsonString.trim();
        if ( trimmed.startsWith( "{") || trimmed.startsWith("["))
            return trimmed;
        else
            return ("{ "+ trimmed + "}");
    }
}
