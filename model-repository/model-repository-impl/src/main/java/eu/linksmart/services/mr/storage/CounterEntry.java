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
@Table(name="ENTRYCOUNTER")
public class CounterEntry {
	
	@Id
    @Column(name="ID", nullable=false, unique=true, updatable=false)
    @Basic(optional=false)
    private String identifier;
	
	@Column(name="COUNTER", nullable=false, unique=false, updatable=true)
    @Basic(optional=false)
    private int counter;

    public CounterEntry() {
    }

    public CounterEntry(String identifier, int counter) {
        this.identifier = identifier;
        this.counter = counter;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

}
