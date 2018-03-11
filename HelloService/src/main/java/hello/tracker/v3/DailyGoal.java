package hello.tracker.v3;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.DayOfWeek;

@Entity
@Table(schema = "TRACKER", name = "DAILY_GOAL")
@Data
@org.hibernate.annotations.Immutable
class DailyGoal {

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

    @OneToOne
    @JoinColumn(
            name = "GOAL_ID",
            insertable = true, updatable = false
    )
    private Goal goal;

    @Enumerated(value = EnumType.STRING)
    private DailyGoalStatus status = DailyGoalStatus.DAILY_GOAL_NOT_COMPLETED;
}
