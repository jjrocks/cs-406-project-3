import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by JJ's Lappy on 5/1/2016.
 */

public class TextReader {

    public static ArrayList<Process> processText(String filepath) {
        ArrayList<Process> processes = new ArrayList<>();
        List<String> unParsedStrings = new ArrayList<>();
        Path path = Paths.get(filepath);
        try {
            unParsedStrings = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String fileLine : unParsedStrings) {
            Scanner scanner = new Scanner(fileLine.replace(",", ""));
            int pid, address;
            boolean write;
            pid = scanner.nextInt();
            address = scanner.nextInt();
            write = scanner.next().equals("W");
            System.out.println("Pid: " + pid + " Address: " + address + " Write? " + write);
            processes.add(new Process(pid, address, write));
        }

        return processes;
    }
}

