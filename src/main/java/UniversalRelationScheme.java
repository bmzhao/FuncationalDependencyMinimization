import java.util.Collection;

/**
 * Created by brianzhao on 6/1/16.
 */
public class UniversalRelationScheme {
    private final Collection<Character> universe;
    private final Collection<FD> fds;

    public UniversalRelationScheme(Collection<Character> universe, Collection<FD> fds) {
        this.fds = fds;
        this.universe = universe;
    }

    public Collection<FD> getFds() {
        return fds;
    }

    public Collection<Character> getUniverse() {
        return universe;
    }
}
