import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Created by brianzhao on 5/23/16.
 */
public class FDUtils {
    public static void main(String[] args) {

        File input = new File("hw63.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        List<FD> fds = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            fds.add(new FD(line[0], line[1]));
        }

        Set<FD> minimum = minimumEquivalent(fds);
        System.out.println(minimum);
        Set<FD> lMinimum = lMinimum(minimum);
        System.out.println(lMinimum);
        Set<FD> circularMiniumum = circularMinimum(lMinimum);
        System.out.println(circularMiniumum);
        Set<FD> rMiniumum = rMinimum(circularMiniumum);
        System.out.println(rMiniumum);
    }

    /**
     * implements closure algorithm 3.3 pg 32
     *
     * @param input the given Functional dependency
     * @param wrt   the set of FDs that the closure is computed with respect to
     * @return set of attributes
     */
    public static List<Character> closure(Collection<Character> input, Collection<FD> wrt) {
        Set<Character> result = new HashSet<>();
        result.addAll(input);
        boolean closed = false;
        while (!closed) {
            closed = true;
            for (FD fd : wrt) {
                Set<Character> lhs = new HashSet<>(fd.lhs);
                Set<Character> rhs = new HashSet<>(fd.rhs);
                if (result.containsAll(lhs) && !result.containsAll(rhs)) {
                    closed = false;
                    result.addAll(rhs);
                }
            }
        }
        List<Character> resultList = new ArrayList<>(result);
        Collections.sort(resultList);
        return resultList;
    }

    public static boolean isRedundant(FD possiblyRedundant, Collection<FD> fds) {
        if (!fds.contains(possiblyRedundant)) {
            throw new RuntimeException("Second argument must contain the first");
        }

        HashSet<FD> fdsWithoutPossiblyRedundant = new HashSet<>(fds);
        fdsWithoutPossiblyRedundant.remove(possiblyRedundant);
        if (fdsWithoutPossiblyRedundant.size() != fds.size() - 1) {
            throw new RuntimeException("Size didn't decrease");
        }

        List<Character> xClosure = closure(possiblyRedundant.lhs, fdsWithoutPossiblyRedundant);
        return xClosure.containsAll(possiblyRedundant.rhs);
    }

    public static Set<FD> minimumEquivalent(Collection<FD> fds) {
        Set<FD> result = new HashSet<>();
        for (FD fd : fds) {
            result.add(new FD(fd.lhs, closure(fd.lhs, fds)));
        }
        return nonredundantEquivalent(result);
    }


    public static Set<FD> nonredundantEquivalent(Collection<FD> fds) {
        Set<FD> result = new HashSet<>(fds);
        for (FD fd : fds) {
            if (isRedundant(fd, result)) {
                result.remove(fd);
            }
        }
        return result;
    }

    /**
     * assuming input has already been l minized and is attribute closed
     * @param fds
     * @return
     */
    public static Set<FD> circularMinimum(Collection<FD> fds) {
        Set<FD> result = new HashSet<>(fds);
        Map<List<Character>, List<List<Character>>> rhsToLhs = new HashMap<>();
        for (FD fd : fds) {
            if (!rhsToLhs.containsKey(fd.rhs)) {
                rhsToLhs.put(fd.rhs, new ArrayList<>());
            }
            rhsToLhs.get(fd.rhs).add(fd.lhs);
        }
        //for each equivalence class
        for (List<Character> rhs : rhsToLhs.keySet()) {
            List<List<Character>> leftHandSides = rhsToLhs.get(rhs);

            List<FD> fdsToRemove = new ArrayList<>();
            for (List<Character> lhs : leftHandSides) {
                fdsToRemove.add(new FD(lhs, rhs));
            }
            result.removeAll(fdsToRemove);

            List<Character> z = new ArrayList<>(rhs);
            for (List<Character> lhs: leftHandSides) {
                z.removeAll(lhs);
            }

            List<FD> fdsToAdd = new ArrayList<>();
            for (int i = 0; i < fdsToRemove.size(); i++) {
                fdsToAdd.add(new FD(fdsToRemove.get(i).lhs, fdsToRemove.get((i+1)%fdsToRemove.size()).lhs));
            }

            if (fdsToAdd.size() > 0) {
                FD first = fdsToAdd.get(0);
                first.rhs.addAll(z);
                Collections.sort(first.rhs);
            } else {
                throw new RuntimeException("Can't have this be size 0");
            }
            result.addAll(fdsToAdd);
        }
        return result;
    }


    public static Set<FD> rMinimum(Collection<FD> fds) {
        Set<FD> result = new HashSet<>(fds);
        for (FD fd : fds) {
            Set<Character> rhs = new HashSet<>(fd.rhs);
            Set<Set<Character>> powerset = Sets.powerSet(rhs);
            List<Set<Character>> powersetList = new ArrayList<>();
            for (Set<Character> subset : powerset) {
                powersetList.add(subset);
            }
            Collections.sort(powersetList, new Comparator<Set<Character>>() {
                @Override
                public int compare(Set<Character> o1, Set<Character> o2) {
                    return o1.size() - o2.size();
                }
            });
            for (Set<Character> w : powersetList) {
                Set<FD> possibleResult = new HashSet<>(result);
                possibleResult.remove(fd);
                possibleResult.add(new FD(fd.lhs, w));
                List<Character> wClosure = closure(fd.lhs, possibleResult);
                if (wClosure.containsAll(fd.rhs)) {
                    result = possibleResult;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * only accepts a minimum, attribute closed collection of FDs
     *
     * @return
     */
    public static Set<FD> lMinimum(Collection<FD> fds) {
        Set<FD> result = new HashSet<>(fds);
        for (FD fd : fds) {
            Set<Character> lhs = new HashSet<>(fd.lhs);
            Set<Set<Character>> powerset = Sets.powerSet(lhs);
            List<Set<Character>> powersetList = new ArrayList<>();
            for (Set<Character> subset : powerset) {
                powersetList.add(subset);
            }
            Collections.sort(powersetList, new Comparator<Set<Character>>() {
                @Override
                public int compare(Set<Character> o1, Set<Character> o2) {
                    return o1.size() - o2.size();
                }
            });
            for (Set<Character> w : powersetList) {
                Set<FD> possibleResult = new HashSet<>(result);
                possibleResult.remove(fd);
                possibleResult.add(new FD(w, fd.rhs));
                List<Character> wClosure = closure(w, result);
                if (wClosure.containsAll(fd.rhs)) {
                    result = possibleResult;
                    break;
                }
            }
        }
        return result;
    }

}
