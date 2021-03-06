package hello.tracker.v5;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(schema = "TRACKER", name = "USER_DAILY_AGENDA")
@Data
@org.hibernate.annotations.Immutable
class DailyAgenda {

    @Embeddable
    static class Id implements Serializable {

        @Column(name = "USER_ID")
        protected Long userId;

        @Column(name = "WEEKDAY")
        protected DayOfWeek dayOfWeek;

        public Id() {
        }

        public Id(Long userId, DayOfWeek dayOfWeek) {
            this.userId = userId;
            this.dayOfWeek = dayOfWeek;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.userId.equals(that.userId)
                        && this.dayOfWeek.equals(that.dayOfWeek);
            }
            return false;
        }

        public int hashCode() {
            return userId.hashCode() + dayOfWeek.hashCode();
        }

    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(
            name = "USER_ID",
            insertable = false, updatable = false
    )
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @MapKeyJoinColumn(name = "ACTIVITY_ID")
    @JoinTable(
        name = "DAILY_AGENDA",
        joinColumns = {
                @JoinColumn(name = "USER_ID"),
                @JoinColumn(name = "WEEKDAY")
        },
        inverseJoinColumns = @JoinColumn(name = "GOAL_ID")
    )
    private Map<Activity,DailyGoal> goals = new HashMap<>();

}
