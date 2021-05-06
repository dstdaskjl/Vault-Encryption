import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FromFile {
    static boolean Does_ID_Exist(char[] mainID, char[] id) throws IOException {
        return File_To_Map(mainID).containsKey(new String(id));
    }

    static Map<String, String> File_To_Map(char[] mainID) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new String(mainID) + ".txt"));
        br.readLine();
        String line = null;
        Map<String, String> map = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] split = line.split(" ");
            map.put(split[0], split[1]);
            line = null;
            split = null;
        }
        return map;
    }

    static List<String> File_To_List(char[] mainID) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new String(mainID) + ".txt"));
        br.readLine();
        String line = null;
        List<String> lst = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            lst.add(line);
            line = null;
        }

        return lst;
    }

    static char[] Get_Master_Pass(char[] mainID) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new String(mainID) + ".txt"));
        return br.readLine().toCharArray();
    }

    static void Write_To_File_Append(char[] fileName, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new String(fileName) + ".txt", true));
        writer.write(text);
        writer.newLine();
        writer.close();
    }

    static void Write_To_File_Append(char[] fileName, List<String> lst) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new String(fileName) + ".txt", true));
        for (String str : lst){
            writer.write(str);
            writer.newLine();
        }

        writer.close();
    }

    static void Write_To_File_Overwrite(char[] mainID, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new String(mainID) + ".txt", false));
        writer.write(text);
        writer.newLine();
        writer.close();
    }

    static void Write_To_File_Overwrite(char[] mainID, List<String> lst, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new String(mainID) + ".txt", false));
        writer.write(text);
        writer.newLine();
        for (String str : lst){
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }
}
