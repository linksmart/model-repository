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
@Table(name="ENTRYSXMI")
public class StorageEntryXmi {

	@Id
    @Column(name="ID", nullable=false, unique=true, updatable=false)
    @Basic(optional=false)
    private String key;

    @Column(name="XML_TEXT", length=64000)
    @Basic(optional=false)
    private String value;

    public StorageEntryXmi() {
    }

    public StorageEntryXmi(String key, String xmlText) {
        this.key = key;
        this.value = xmlText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String xmlText) {
        this.value = xmlText;
    }
}
