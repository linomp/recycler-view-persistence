package systems.mp.votacioneslmp;

import java.io.Serializable;
import java.util.Objects;

public class Candidate implements Serializable {

    private String name;
    private int voteCount;

    public Candidate() {
    }

    public Candidate(String name, int voteCount) {
        this.name = name;
        this.voteCount = voteCount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(getName(), candidate.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }

    public void increaseVoteCount() {
        voteCount++;
    }
}
