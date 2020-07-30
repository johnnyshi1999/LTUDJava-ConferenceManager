package Database.Hibernate.Entities;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.*;

//@Entity
//@Table(name = "conference",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"hold_date"}))

public class Conference implements Serializable {

    private int id;
    StringProperty name = new SimpleStringProperty();
    StringProperty shortDes = new SimpleStringProperty();
    StringProperty detailDes = new SimpleStringProperty();
    ObjectProperty<Location> location = new SimpleObjectProperty<Location>();
    ObjectProperty<Date> holdDate = new SimpleObjectProperty<Date>();
    IntegerProperty attendeeLimit = new SimpleIntegerProperty();
    StringProperty imgPath = new SimpleStringProperty();

    Set<Attending> attendeeSet = new HashSet<Attending>(0);

    public Conference() {}
//    public Conference(int ID, String Name, String ShortDes, String DetailDes,
//                      Location ConferenceLocation, Date HoldDate, int Limit, String ImagePath) {
//        id = ID;
//        shortDes = ShortDes;
//        detailDes = DetailDes;
//        location = ConferenceLocation;
//        holdDate = HoldDate;
//        attendeeLimit= Limit;
//        attendeeSet = new HashSet<Attending>(0);
//        imgPath = ImagePath;
//    }

//    @Id

//    @Column(name = "conference_id")
    public int getId() {
        return id;
    }

    public void setId(int ID) {
        id = ID;
    }

    //@Column(name = "conference_name", length = 100, nullable = false)
    public String getName() {
        return name.get();
    }

    public void setName(String ConferenceName) {
        name.set(ConferenceName);
    }

    //@Column(name = "short_des")
    public String getShortDes() {
        return shortDes.get();
    }

    public void setShortDes(String ShortDescription) {
        shortDes.set(ShortDescription);
    }

    //@Column(name = "detail_des")
    public String getDetailDes() {
        return detailDes.get();
    }

    public void setDetailDes(String DetailDescription) {
        detailDes.set(DetailDescription);
    }

    //@Column(name = "hold_date", nullable = false)
    public Date getHoldDate() {
        return holdDate.get();
    }

    public void setHoldDate(Date date) {
        holdDate.set(date);
    }


    //@ManyToOne
    //@JoinColumn(nullable=false)
    public Location getLocation() {
        return location.get();
    }

    public void setLocation(Location newLocation) {
        location.set(newLocation);
    }

    //@Column(name = "attendee_limit")
    public int getAttendeeLimit() {
        return attendeeLimit.get();
    }

    public void setAttendeeLimit(int Limit) {
        attendeeLimit.set(Limit);
    }

//    //@ManyToMany()
//    //@JoinTable(
//            name = "attending",
//            joinColumns = @JoinColumn(name = "conference_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
    public Set<Attending> getAttendeeSet() {
        return attendeeSet;
    }

    public void setAttendeeSet(Set<Attending> set) {
        attendeeSet = set;
    }

    public String getImgPath() {
        return imgPath.get();
    }

    public void setImgPath(String imgPath) {
        this.imgPath.set(imgPath);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty shortDesProperty() {
        return shortDes;
    }

    public StringProperty detailDesProperty() {
        return detailDes;
    }

    public ObjectProperty<Location> locationProperty() {
        return location;
    }

    public ObjectProperty<Date> holdDateProperty() {
        return holdDate;
    }

    public StringProperty imgPathProperty() {
        return imgPath;
    }

    public IntegerProperty limitProperty() { return attendeeLimit; }


}
