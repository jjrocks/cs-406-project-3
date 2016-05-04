/**
 * Created by JJ on 5/3/2016.
 */
public class Result {

    int pageNumber;
    Process process;
    int frameNumber;
    boolean pageFault;
    int physicalAddress;
    boolean isReplacement = false;
    boolean writeToMemory = false;

    public Result(Process process, int frameNumber, boolean pageFault, boolean isReplacement, boolean writeToMemory) {
        this.process = process;
        this.frameNumber = frameNumber;
        this.pageFault = pageFault;
        this.isReplacement = isReplacement;
        this.writeToMemory = writeToMemory;


        pageNumber = process.getAddress()/FrameLoader.PAGE_SIZE;
        int offset = process.getAddress() % FrameLoader.PAGE_SIZE;
        physicalAddress = frameNumber * FrameLoader.PAGE_SIZE + offset;
    }

    @Override
    public String toString() {
        String result =   "";
        if (pageFault) {
            result = "loaded page #" + pageNumber + " to frame #" + frameNumber;
            result += (isReplacement) ? " with replacement" : " with no replacement";
            result += (writeToMemory) ? "\n\tNeed to write frame #" + frameNumber + " to memory" : "";
        } else {
            result += "no page fault. accessed frame #" + frameNumber;
        }
         result += "\n\tVirtual address: " + process.getAddress() + " -> Physical Address: " + physicalAddress;
        return result;
    }
}
