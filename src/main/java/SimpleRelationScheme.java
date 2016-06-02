import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brianzhao on 6/1/16.
 */

/**
 * intended to only have a single functional dependency
 */
public class SimpleRelationScheme {
    private final Collection<Character> attributes;
    private final FD functionalDependency;

    public SimpleRelationScheme(Collection<Character> attributes, FD functionalDependency) {
        this.attributes = new ArrayList<>(attributes);
        this.functionalDependency = functionalDependency;
    }

    public SimpleRelationScheme(FD functionalDependency) {
        this.functionalDependency = functionalDependency;
        Set<Character> attributes = new HashSet<>();
        attributes.addAll(functionalDependency.lhs);
        attributes.addAll(functionalDependency.rhs);
        this.attributes = attributes;
    }

    public Collection<Character> getAttributes() {
        return attributes;
    }

    public FD getFunctionalDependency() {
        return functionalDependency;
    }
}
