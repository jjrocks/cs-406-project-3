import java.util.ArrayList;

public class FrameLoader {

	int timeStamp = 0;
	ArrayList<Process> processes;
	ReplacementAlgorithm algorithm;
    public static int PAGE_SIZE = 32;

    public FrameLoader(ReplacementAlgorithm algorithm, ArrayList<Process> processes) {
        this.algorithm = algorithm;
        this.processes = processes;
    }

    public void run() {

    	int pageFaults = 0;
		int numWrites = 0;

		for (Process process : processes) {
            Result result = algorithm.execute(process, timeStamp);
            System.out.println(result);
			if (result.pageFault) {
				pageFaults += 1;
			}
			if(result.writeToMemory) {
				numWrites += 1;
			}
			timeStamp++;
		}
        System.out.println("Number of page faults: " + pageFaults + ". Number of memory accesses: " + (pageFaults+numWrites));
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

			PAGE_SIZE = pageSize;

			//get list of memory accesses
			memAccesses = TextReader.processText(args[2]);

			//run

			int numFrames = (int) Math.pow(2,11) / pageSize;

			String alg = args[0];
			FrameLoader frameLoader = null;
			switch(alg) {
				case "Optimal":
					frameLoader = new FrameLoader(new Optimal(memAccesses, numFrames), memAccesses);
					break;
				case "FIFO":
					frameLoader = new FrameLoader(new FirstInFirstOut(numFrames), memAccesses);
					break;
				case "LRU":
					frameLoader = new FrameLoader(new LeastRecentlyUsed(numFrames), memAccesses);
					break;
				case "2Chance":
					frameLoader = new FrameLoader(new SecondChance(numFrames), memAccesses);
					break;
				case "En2Chance":
					frameLoader = new FrameLoader(new EnhancedSecondChance(numFrames), memAccesses);
					break;
				case "LFU":
					frameLoader = new FrameLoader(new LeastFrequentlyUsed(numFrames), memAccesses);
					break;
			}

            //FrameLoader frameLoader = new FrameLoader(new LeastRecentlyUsed(2), memAccesses);
            //FrameLoader frameLoader = new FrameLoader(new LeastFrequentlyUsed(numFrames), memAccesses);
 
			if(frameLoader == null) {
				System.out.println("Invalid algorithm choice. Valid options are: "+
					"Optimal, FIFO, LRU, 2Chance, En2Chance, LFU");
				return;
			}

            frameLoader.run();

		} 
		else {
			System.out.println("Invalid input. Please enter 1) the replacecment algorithm, "+
				"2) the size of a page in bytes, and 3) the input file name.");
			return;
		}
	}

}