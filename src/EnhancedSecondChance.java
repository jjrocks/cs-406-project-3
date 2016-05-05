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
            frame.overwriteRead = false;
            if(process.isWrite()) {
                frame.overwriteWrite = false;
                frames.get(frames.indexOf(frame)).process.setWrite(true);
            }
        } else {
            isReplacement = true;
            if (frames.size() < maxFrameSize) {
                frames.add(frame);
                currentProcess = (currentProcess + 1) % maxFrameSize;
            } else {
                Frame oldFrame = getLeastUsedFrame();
                writeToMemory = oldFrame.process.isWrite();
                frames.set(frames.indexOf(oldFrame), frame);

            }
        }
        return new Result(process, frames.indexOf(frame), pageFault, isReplacement, writeToMemory);
    }

    private Frame getLeastUsedFrame() {
        boolean frameFound = false;
        int startProcess = currentProcess;
        Frame oldestFrame = null;
        int loopCount = 0;

        // Our order goes neither written or read, written but not read, read but not written, written and read
        while(!frameFound) {
            Frame frame = frames.get(currentProcess);
            currentProcess = (currentProcess + 1) % maxFrameSize;
            if(frame.overwriteRead ) {
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
            if (currentProcess == startProcess) {
                loopCount++;
            }
        }
        return oldestFrame;
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
