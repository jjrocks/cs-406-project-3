import java.util.*;

/**
 * Created by JJ on 5/2/2016.
 */
public class FirstInFirstOut implements ReplacementAlgorithm {

    ArrayList<Process> processes;
    ArrayList<Frame> frames;
    int maxFrameSize = 0;

    public FirstInFirstOut(int maxFrameSize) {
        processes = new ArrayList<>();
        this.maxFrameSize = maxFrameSize;
        frames = new ArrayList<>(maxFrameSize);
    }

    @Override
    public Result execute(Process process, int timestamp) {
        Frame frame = new Frame(process, timestamp);
        boolean pageFault;
        boolean isReplacement = false;
        boolean writeToMemory = false;
        if(frames.contains(frame)) {
            pageFault = false;
        } else {
            pageFault = true;
            if (frames.size() < maxFrameSize) {
                frames.add(frame);
            } else {
                Frame oldestFrame = getLastUsedFrame();
                isReplacement = true;
                writeToMemory = oldestFrame.process.isWrite();
                frames.set(frames.indexOf(oldestFrame), frame);
            }
        }

        Result result = new Result(process, frames.indexOf(frame), pageFault, isReplacement, writeToMemory);
        return result;
    }

    private Frame getLastUsedFrame() {
        int lastUsed = Integer.MAX_VALUE;
        Frame oldestFrame = null;
        for (Frame frame : frames) {
            if (frame.lastUsed < lastUsed) {
                lastUsed = (frame.lastUsed < lastUsed) ? frame.lastUsed : lastUsed;
                oldestFrame = frame;
            }
        }
        return oldestFrame;
    }

    private class Frame {
        Process process;
        int lastUsed;

        public Frame(Process process, int lastUsed) {
            this.process = process;
            this.lastUsed = lastUsed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Frame frame = (Frame) o;

            if (!process.equals(frame.process)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return process.hashCode();
        }
    }
}
