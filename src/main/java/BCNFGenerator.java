import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by brianzhao on 6/1/16.
 */
public class BCNFGenerator {
    public static void main(String[] args) {
        RelationScheme universal = readInput("hw63.txt");
        Set<FD> g = (new FDMinimizer(universal.getFunctionalDependencies())).lRCircularMinimum();
        System.out.println("Circular LR Minimum: ");
        System.out.println(g);

        List<RelationScheme> relationSchemes = new ArrayList<>();
        g.forEach(fd -> relationSchemes.add(new RelationScheme(fd)));

        boolean needKey = true;

        for (RelationScheme relationScheme : relationSchemes) {
            Set<Character> ithUniverse = new HashSet<>(relationScheme.getAttributes());
            if (CollectionUtils.isEqualCollection(FDUtils.closure(ithUniverse, g), universal.getAttributes())) {
                needKey = false;
                break;
            }
        }

        if (needKey) {
            KeyGenerator keyGenerator = new KeyGenerator(universal.getFunctionalDependencies(), universal.getAttributes());
            List<Set<Character>> keys = new ArrayList<>(keyGenerator.getKeys());
            keys.sort((o1, o2) -> o1.size() - o2.size());
            Set<Character> key = keys.get(0);
            relationSchemes.add(new RelationScheme(key, new HashSet<FD>()));
        }

        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < relationSchemes.size() - 1; i++) {
                for (int j = i + 1; j < relationSchemes.size(); j++) {
                    RelationScheme relation1 = relationSchemes.get(i);
                    RelationScheme relation2 = relationSchemes.get(j);
                    if (CollectionUtils.isEqualCollection(relation1.getAttributes(), relation2.getAttributes())) {
                        done = false;
                        relationSchemes.remove(j);
                        relation1.getFunctionalDependencies().addAll(relation2.getFunctionalDependencies());
                        j--;
                    }
                }
            }
        }

        System.out.println(relationSchemes.toString().replaceAll("},", "},\n"));

    }

    private static RelationScheme readInput(String filename) {
        File file = new File(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int lineNumber = 0;
        Collection<Character> attributes = new ArrayList<>();
        List<FD> fds = new ArrayList<>();
        while (scanner.hasNextLine()) {
            if (lineNumber == 0) {
                for (char c : scanner.nextLine().toCharArray()) {
                    attributes.add(Character.toUpperCase(c));
                }
            } else {
                String[] line = scanner.nextLine().split(",");
                fds.add(new FD(line[0], line[1]));
            }
            lineNumber++;
        }
        return new RelationScheme(attributes, fds);
    }
}
