package Database.Hibernate.Entities;

import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "location",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"location_name", "address"}))
public class Location {
    int id;
    String name;
    String address;
    int capacity;

    Set<Conference> conferenceSet = new HashSet<>(0);

//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    @Column(name = "location_id")
    public int getId() {
        return id;
    }

    public void setId(int ID) {
        id = ID;
    }

//    @Column(name = "location_name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = new String(Name);
    }

//    @Column(name = "address", length = 100, nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String Address) {
        address = new String(Address);
    }

//    @Column(name = "capacity")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int Capacity) {
        capacity = Capacity;
    }

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    public Set<Conference> getConferenceSet() {
        return conferenceSet;
    }
    public void setConferenceSet(Set<Conference> set) {
        conferenceSet = set;
    }

    @Override
    public String toString() {
        return name;
    }

}
