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
    private String modelName;
	
	@Column(name="COUNTER", nullable=false, unique=false, updatable=true)
    @Basic(optional=false)
    private int counter;

    public CounterEntry() {
    }

    public CounterEntry(String modelName, int counter) {
        this.modelName = modelName;
        this.counter = counter;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

}
