import java.util.*;
import java.lang.*;

public class Optimal impements ReplacementAlgorithm {

	ArrayList<Process> memAccesses;
	ArrayList<Frame> frames;
	int maxFrames;
	int curProcess;

	public Optimal (ArrayList<Process> accesses, int numFrames) {
		memAccesses = accesses;
		frames = new ArrayList<Frame>(numFrames); 
		maxFrames = numFrames;
		curProcess = -1;
	}

	public Result execute(Process process, int timeStamp) {
		curProcess++;

		if(curProcess >= memAccesses.size()) {
			throw new IllegalStateException("Tried to run execute after sequence was already completed.");
		}

		int proc_id = process.getPid();
		int page_num = process.getPageNumber();

		Frame frame = new Frame(proc_id, page_num);

		int frameNum = frames.indexOf(frame);
		int numFrames = frames.size()

		if(frameNum > -1) {
			//no page fault
			return new Result(page_num, proc_id, frameNum, false);
		}
		else if(numFrames < maxFrames) {
			//page fault, but there are empty frames
			frames.add(frame);
			return new Result(page_num, proc_id, numFrames, true);
		}
		else {
			//page fault, and we must replace a frame
			int frameNumToReplace = -1;
			int farthestTimeUntilNextUse = -1;
			for(int i=0; i<numFrames; i++) {
				Frame f = frames.get(i);
				int nextUse = Integer.MAX_VALUE;
				for(int j=curProcess+1; j<memAccesses.size(); j++) {
					Proccess p = memAccesses.get(j);
					if(p.getPid() == f.proc_id && p.getPageNumber() == f.page_num) {
						nextUse = j;
						break;
					}
				}
				if(nextUse > farthestTimeUntilNextUse) {
					frameNumToReplace = i;
					farthestTimeUntilNextUse = nextUse;
				}
			}

			frames.set(frameNumToReplace, frame);
			return new Result(page_num, proc_id, frameNumToReplace, true);
		}
	}

	private class Frame {
		public int proc_id;
		public int page_num;

		public Frame (int pid, pg_num) {
			proc_id = pid;
			page_num = pg_num;
		}

		@Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Frame frame = (Frame) o;

            if(this.proc_id == frame.proc_id && this.page_num == frame.page_num)
            {
            	return true;
            }
            return false;
        }
	}
}