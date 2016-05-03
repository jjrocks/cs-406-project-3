import java.util.*;

/**
 * Created by JJ on 5/2/2016.
 */
public class LeastRecentlyUsed implements ReplacementAlgorithm {

    ArrayList<Process> processes;
    ArrayList<Frame> frames;
    int frameSize = 0;
    int currentProcess = 0;

    public LeastRecentlyUsed(ArrayList<Process> processes, int frameSize) {
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
            System.out.println("no page fault. accessed frame " + frames.indexOf(frame));
            frames.get(frames.indexOf(frame)).lastUsed = currentProcess;
            return true;
        } else {
            if (frames.size() < frameSize) {
                frames.add(frame);
                System.out.println("loaded page#1 of process #" + process.getPid() + " with no replacement.");
            } else {
                Frame oldestFrame = getLastUsedFrame();
                System.out.println("loaded page #1 of processes #" + process.getPid() + " to frame #" + frames.indexOf(getLastUsedFrame()) + " with replacement");
                frames.remove(oldestFrame);
                frames.add(frame);
            }
        }
        return false;
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
