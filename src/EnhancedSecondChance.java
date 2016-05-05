import java.util.ArrayList;

/**
 * Created by JJ on 5/3/2016.
 */
public class EnhancedSecondChance implements ReplacementAlgorithm {

    ArrayList<Frame> frames;
    int currentProcess = 0;
    int maxFrameSize = 0;

    public EnhancedSecondChance(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        frames = new ArrayList<>();
    }


    @Override
    public Result execute(Process process, int timeStamp) {
        Frame frame = new Frame(process);
        boolean pageFault = true;
        boolean isReplacement = false;
        boolean writeToMemory = false;
        if (frames.contains(frame)) {
            pageFault = false;
            frames.get(frames.indexOf(frame)).overwriteRead = false;
            if(process.isWrite()) {
                frames.get(frames.indexOf(frame)).overwriteWrite = false;
                frames.get(frames.indexOf(frame)).process.setWrite(true);
            }
        } else {
            if (frames.size() < maxFrameSize) {
                frames.add(frame);
                currentProcess = frames.indexOf(frame);
                currentProcess = (currentProcess + 1) % maxFrameSize;
            } else {
                isReplacement = true;
                Frame oldFrame = getLeastUsedFrame();
                writeToMemory = oldFrame.process.isWrite();
                frames.set(frames.indexOf(oldFrame), frame);

            }
        }
        printFrames();
        return new Result(process, frames.indexOf(frame), pageFault, isReplacement, writeToMemory);
    }

    private Frame getLeastUsedFrame() {
        boolean frameFound = false;
        int startProcess = currentProcess;
        Frame oldestFrame = null;
        int loopCount = -1;

        // Our order goes neither written or read, written but not read, read but not written, written and read
        while (!frameFound) {
            Frame frame = frames.get(currentProcess);
            if (currentProcess == startProcess) {
                loopCount++;
            }
            currentProcess = (currentProcess + 1) % maxFrameSize;
            if (frame.overwriteRead) {
                if (frame.overwriteWrite) {
                    return frame;
                }
                // At this point there are no 0,0 at all so we have to find the first frame.
                if (loopCount > 0) {
                    return frame;
                }
            } else {
                if (loopCount > 0) {
                    frame.overwriteRead = true;
                }
            }
        }
        return oldestFrame;
    }

    private void printFrames() {
        String  toPrint = "[";
        for (int i = 0; i < frames.size() ; i++) {
            Frame frame = frames.get(i);
            toPrint += frame.process.getPid() + "." + frame.process.getPageNumber();
            toPrint += (frame.overwriteRead) ? "" : "R";
            toPrint += (frame.overwriteWrite) ? "" : "W";
            toPrint += (i == frames.size() - 1) ? "" : ", ";
        }
        toPrint += "]";
        System.out.println(toPrint);
    }


    private class Frame {
        Process process;
        boolean overwriteRead = false;
        boolean overwriteWrite = true;

        public Frame(Process process) {
            this.process = process;
            overwriteWrite = !process.isWrite();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Frame frame = (Frame) o;

            return process.equals(frame.process);

        }

        @Override
        public int hashCode() {
            return process.hashCode();
        }
    }
}
