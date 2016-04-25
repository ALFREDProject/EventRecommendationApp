package alfred.eu.eventrecommendationapp.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import eu.alfred.api.event.model.Organizer;
import eu.alfred.api.event.model.Review;
import eu.alfred.api.event.model.Tickets;
import eu.alfred.api.event.model.Venue;
import eu.alfred.api.personalization.model.Language;

/**
 * Created by thardes on 25/04/2016.
 */

//@JsonIgnoreProperties(ignoreUnknown=true)
public class Event {

    private String eventID;
    private Organizer organizer;
    private Venue venue;
    private Date end_date;
    private Date modified;
    private Date created;
    private Date start_date;
    private int num_attendee_rows; //int
    private int is_public;		  //bool
    private String repeats;			  //int
    private String capacity;		  //int
    private String timezone_offset;
    private String title;
    private String url;
    private String locale;
    private String status;			  //Some enum
    private String timezone;		  // Java timezone
    private String description;
    private List<Tickets> tickets;
    private List<String> tags;		  // Some enum
    private List<String> categories;  // Some enum
    private List<String> accessibility;//MobilityLevel
    private Language language;		   // Why not use the enum
    private List<Review> reviews;
    private List<String> participants;  //List of "Contact"?

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public Organizer getOrganizer() {
        return organizer;
    }
    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }
    public Venue getVenue() {
        return venue;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    public Date getEnd_date() {
        return end_date;
    }
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
    public Date getModified() {
        return modified;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getStart_date() {
        return start_date;
    }
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    public int getNum_attendee_rows() {
        return num_attendee_rows;
    }
    public void setNum_attendee_rows(int num_attendee_rows) {
        this.num_attendee_rows = num_attendee_rows;
    }
    public int getIs_public() {
        return is_public;
    }
    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }
    public String getRepeats() {
        return repeats;
    }
    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }
    public String getCapacity() {
        return capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public String getTimezone_offset() {
        return timezone_offset;
    }
    public void setTimezone_offset(String timezone_offset) {
        this.timezone_offset = timezone_offset;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Tickets> getTickets() {
        return tickets;
    }
    public void setTickets(List<Tickets> tickets) {
        this.tickets = tickets;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getCategories() {
        return categories;
    }
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    public List<String> getAccessibility() {
        return accessibility;
    }
    public void setAccessibility(List<String> accessibility) {
        this.accessibility = accessibility;
    }
    public Language getLanguage() {
        return language;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public List<String> getParticipants() {
        return participants;
    }
    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((eventID == null) ? 0 : eventID.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (eventID == null) {
            if (other.eventID != null)
                return false;
        } else if (!eventID.equals(other.eventID))
            return false;
        return true;
    }

}
