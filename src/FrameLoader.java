import java.util.ArrayList;

public class FrameLoader {

	int pageFaults = 0;
	int timeStamp = 0;
	ArrayList<Process> processes;
	ReplacementAlgorithm algorithm;

    public FrameLoader(ReplacementAlgorithm algorithm, ArrayList<Process> processes) {
        this.algorithm = algorithm;
        this.processes = processes;
    }

    public void run() {
		for (Process process : processes) {
			if (algorithm.execute()) {
				pageFaults += 1;
			}
		}
        System.out.println("Page faults: " + pageFaults);
	}
	
	public static void main(String[] args) {
		//Your main function should accept three strings. 
		//One which states which page replacement algorithm to use 
		//(e.g. fifo), one which states the size of a page 
		//(between the allowed range) and the last which states the input file name. 

		//make sure we have the appropriate number of arguments 
		ReplacementAlgorithm replaceAlg;
		int pageSize;
		ArrayList<Process> memAccesses;
		if(args.length == 3) {

			//TODO: select replacement algorithm

			//get page size
			try {
				pageSize = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e) {
				pageSize = -1;
			}
			//this checks if it's in range and if it's a power of 2 with bit operations
			if(pageSize < 32 || pageSize > 512 || ((pageSize & (pageSize - 1)) != 0)) {
				System.out.println("Invalid page size. Please enter an integer page size in bytes "+
					"which is a power of 2 from 32 to 512.");
				return;
			}

			//get list of memory accesses
			memAccesses = TextReader.processText(args[2]);
            FrameLoader frameLoader = new FrameLoader(new FirstInFirstOut(memAccesses, 5), memAccesses);
            frameLoader.run();

		} 
		else {
			System.out.println("Invalid input. Please enter 1) the replacecment algorithm, "+
				"2) the size of a page in bytes, and 3) the input file name.");
			return;
		}
	}

}