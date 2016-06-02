import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by brianzhao on 6/1/16.
 */
public class BCNFGenerator {
    public static void main(String[] args) {
        UniversalRelationScheme universal = readInput("hw61.txt");
        Set<FD> g = (new FDMinimizer(universal.getFds())).lRCircularMinimum();
        System.out.println("Circular LR Minimum: ");
        System.out.println(g);

        List<SimpleRelationScheme> relationSchemes = new ArrayList<>();
        g.forEach(fd -> relationSchemes.add(new SimpleRelationScheme(fd)));

        boolean needKey = true;

        for (SimpleRelationScheme relationScheme : relationSchemes) {
            Set<Character> ithUniverse = new HashSet<>(relationScheme.getAttributes());
            if (FDUtils.closure(ithUniverse, g).containsAll(universal.getUniverse())) {
                needKey = false;
                break;
            }
        }



    }

    private static UniversalRelationScheme readInput(String filename) {
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
        return new UniversalRelationScheme(attributes, fds);
    }
}
