import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brianzhao on 6/1/16.
 */

/**
 * intended to only have a single functional dependency
 */
public class RelationScheme {
    private final Set<Character> attributes;
    private final Set<FD> functionalDependencies;

    public RelationScheme(Collection<Character> attributes, Collection<FD> functionalDependency) {
        this.attributes = new HashSet<>(attributes);
        this.functionalDependencies = new HashSet<>(functionalDependency);
    }

    public RelationScheme(FD functionalDependency) {
        this.functionalDependencies = new HashSet<>();
        this.functionalDependencies.add(functionalDependency);
        Set<Character> attributes = new HashSet<>();
        attributes.addAll(functionalDependency.lhs);
        attributes.addAll(functionalDependency.rhs);
        this.attributes = attributes;
    }

    public Collection<Character> getAttributes() {
        return attributes;
    }

    public Set<FD> getFunctionalDependencies() {
        return functionalDependencies;
    }

    @Override
    public String toString() {
        return "RelationScheme{" +
                "attributes=" + attributes +
                ", functionalDependencies=" + functionalDependencies +
                '}';
    }


}
