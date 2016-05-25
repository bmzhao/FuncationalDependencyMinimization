import com.google.common.collect.Collections2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * Created by brianzhao on 5/24/16.
 */
public class KeyGenerator {
    public static void main(String[] args) {
        Set<Character> universe = new HashSet<>();
        for(char attribute = 'A'; attribute <= 'I'; attribute++) {
            universe.add(attribute);
        }
        universe.remove('F');

        Set<Character> potentialKeyElements = new HashSet<>(universe);
        potentialKeyElements.removeAll(Arrays.asList('A', 'C', 'I'));


        Set<FD> fds = new HashSet<>();
        fds.add(new FD("EA", "GH"));
        fds.add(new FD("GE", "DH"));
        fds.add(new FD("A", "B"));
        fds.add(new FD("G", "DE"));


        Collection<List<Character>> permutations = Collections2.permutations(potentialKeyElements);
        Set<Set<Character>> keys = new HashSet<>();
        for (List<Character> permutation : permutations) {
            Set<Character> potentialKey = new HashSet<>(universe);
            for (char attribute : permutation) {
                Set<Character> potentialKeyWithoutSpecificAttribute = new HashSet<>(potentialKey);
                potentialKeyWithoutSpecificAttribute.remove(attribute);
                if (CollectionUtils.isEqualCollection(FDMinimization.closure(potentialKeyWithoutSpecificAttribute, fds), universe)) {
                    potentialKey.remove(attribute);
                }
            }
            keys.add(potentialKey);
        }
        System.out.println(keys);
    }
}
