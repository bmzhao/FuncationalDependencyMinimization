import java.util.*;

/**
 * Created by brianzhao on 5/23/16.
 */
public class FD {
    List<Character> lhs = new ArrayList<Character>();
    List<Character> rhs = new ArrayList<Character>();

    public FD(String lhs, String rhs) {
        char[] lhsArray = lhs.toUpperCase().toCharArray();
        char[] rhsArray = rhs.toUpperCase().toCharArray();
        for (char c : lhsArray) {
            this.lhs.add(c);
        }
        for (char c : rhsArray) {
            this.rhs.add(c);
        }
        Collections.sort(this.lhs);
        Collections.sort(this.rhs);
    }

    public FD(Collection<Character> lhs, Collection<Character> rhs) {
        this.lhs = new ArrayList<>(lhs);
        this.rhs = new ArrayList<>(rhs);
        Collections.sort(this.lhs);
        Collections.sort(this.rhs);
    }


    private String fdListToString(List<Character> input) {
        StringBuilder stringBuilder = new StringBuilder();
        input.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FD fd = (FD) o;

        if (!lhs.equals(fd.lhs)) return false;
        return rhs.equals(fd.rhs);

    }

    @Override
    public int hashCode() {
        int result = lhs.hashCode();
        result = 31 * result + rhs.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return fdListToString(lhs) + " -> " + fdListToString(rhs);
    }
}
