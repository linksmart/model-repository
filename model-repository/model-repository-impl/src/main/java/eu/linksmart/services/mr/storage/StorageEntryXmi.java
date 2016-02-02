package eu.linksmart.services.mr.storage;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*
* @author hrasheed
*/
@Entity
@Table(name="StorageEntryXmi")
public class StorageEntryXmi {

	@Id
    @Column(name="key", nullable=false, unique=true, updatable=false)
    @Basic(optional=false)
    private String key;
	
	@Column(name="name", nullable=false, unique=false, updatable=false)
    @Basic(optional=false)
    private String name;
	
	@Column(name="version", nullable=false, unique=false, updatable=false)
    @Basic(optional=false)
    private int version;

    @Column(name="XML_TEXT", length=64000)
    @Basic(optional=false)
    private String value;

    public StorageEntryXmi() {
    }

    public StorageEntryXmi(String key, String name, int version, String xmlText) {
        this.key = key;
        this.name = name;
        this.version = version;
        this.value = xmlText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String xmlText) {
        this.value = xmlText;
    }
}
