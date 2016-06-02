import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by brianzhao on 6/1/16.
 */
public class FDMinimizer {
    private List<FD> fds;

    public FDMinimizer(Collection<FD> fds) {
        this.fds = new ArrayList<>(fds);
    }

    public Set<FD> minimumEquivalent(){
        return FDUtils.minimumEquivalent(fds);
    }

    public Set<FD> nonRedundantEquivalent(){
        return FDUtils.nonredundantEquivalent(fds);
    }

    public Set<FD> circularMinimum(){
        return FDUtils.circularMinimum(fds);
    }

    public Set<FD> rMinimum(){
        return FDUtils.rMinimum(fds);
    }

    public Set<FD> lMinimum(){
        return FDUtils.lMinimum(fds);
    }

    public Set<FD> lRCircularMinimum(){
        return FDUtils.rMinimum(FDUtils.circularMinimum(FDUtils.lMinimum(FDUtils.minimumEquivalent(fds))));
    }
}
