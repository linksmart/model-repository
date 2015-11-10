package eu.linksmart.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rachev on 05.11.2015.
 */
public enum LSClassStereotype {

    DEVICE          ("Device"),
    RESOURCE        ("Resource"),
    PROTOCOL        ("Protocol"),
    ENDPOINT        ("Endpoint"),
    RESTENDPOINT    ("RESTEndpoint"),
    MQTTENDPOINT    ("MQTTEndpoint");


    private final String stereotypeName;

    private static final Map<String, LSClassStereotype> nameToValueMap = new HashMap<String, LSClassStereotype>();

    static {
        for (LSClassStereotype value : EnumSet.allOf(LSClassStereotype.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

    LSClassStereotype(String value) {
        this.stereotypeName=value;
    }

    public final String getStereotypeName(){
        return stereotypeName;
    }

    public static LSClassStereotype forStereotypeName(String name) {
        return nameToValueMap.get( name);
    }

    public static boolean isMember(String name) {
        return nameToValueMap.containsKey( name);
    }
}
