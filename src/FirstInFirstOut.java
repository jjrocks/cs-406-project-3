import java.util.*;

/**
 * Created by JJ on 5/2/2016.
 */
public class FirstInFirstOut implements ReplacementAlgorithm {

    ArrayList<Process> processes;
    ArrayList<Frame> frames;
    int frameSize = 0;
    int currentProcess = 0;

    public FirstInFirstOut(ArrayList<Process> processes, int frameSize) {
        this.processes = processes;
        this.frameSize = frameSize;
        frames = new ArrayList<>(frameSize);
    }

    @Override
    public boolean execute() {
        Process process = processes.get(currentProcess);
        Frame frame = new Frame(process, currentProcess);
        currentProcess++;
        if(frames.contains(frame)) {
            return true;
        } else {
            if (frames.size() < frameSize) {
                frames.add(frame);
            } else {
                Frame oldestFrame = getLastUsedFrame();
                frames.remove(oldestFrame);
                frames.add(frame);
            }
        }
        return false;
    }

    private Frame getLastUsedFrame() {
        int lastUsed = -1;
        Frame oldestFrame = null;
        for (Frame frame : frames) {
            lastUsed = (frame.lastUsed < lastUsed) ? frame.lastUsed : lastUsed;
            oldestFrame = frame;
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
