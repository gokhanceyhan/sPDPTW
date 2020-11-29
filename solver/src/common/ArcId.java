package common;

import java.util.Objects;

public class ArcId {

    private int fromLocationId;
    private int toLocationId;

    public ArcId(int fromLocationId, int toLocationId) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(int fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(int toLocationId) {
        this.toLocationId = toLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArcId arc = (ArcId) o;
        return getFromLocationId() == arc.getFromLocationId() &&
                getToLocationId() == arc.getToLocationId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromLocationId(), getToLocationId());
    }
}
