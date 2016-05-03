/**
 * Created by JJ on 5/3/2016.
 */
public class Result {

    int pageNumber;
    int processNumber;
    int frameNumber;
    boolean pageFault;

    public Result(int pageNumber, int processNumber, int frameNumber, boolean pageFault) {
        this.pageNumber = pageNumber;
        this.processNumber = processNumber;
        this.frameNumber = frameNumber;
        this.pageFault = pageFault;
    }
}
