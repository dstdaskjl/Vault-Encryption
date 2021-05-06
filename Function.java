import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.*;

public class Function {
    static char[] GetText() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        char[] text = r.readLine().toCharArray();
        System.out.println();
        return text;
    }

    static char[] GetText(String str) throws IOException {
        System.out.print("Please enter the " + str + ": ");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        char[] text = r.readLine().toCharArray();
        while (text.length < 3){
            System.out.println("\nThe " + str + " should be longer.\n");
            System.out.print("Please enter the " + str + ": ");
            text = r.readLine().toCharArray();
        }
        System.out.println();
        return text;
    }

    static char[] GetPass(String str){
        System.out.print("Please enter the " + str + ": ");
        char[] pass = System.console().readPassword();
        while (pass.length < 7){
            System.out.println("\nThe " + str + " should be longer.\n");
            System.out.print("Please enter the " + str + ": ");
            pass = System.console().readPassword();
        }
        System.out.println();
        return pass;
    }

    static boolean Ask_Yes_No() throws IOException {
        String text = new String(GetText());
        while (!text.toUpperCase().equals("Y") && !text.toUpperCase().equals("N")){
            System.out.print("Please enter either Y or N: ");
            text = new String(GetText());
        }
        if (text.toUpperCase().equals("Y")) return true;
        else return false;
    }

    static String Ask_Choice() throws IOException {
        String choice;
        List<String> choiceList = Arrays.asList("1", "2", "3", "4", "5");

        System.out.println("How can I help you?");
        System.out.println("        1. Store a new ID");
        System.out.println("        2. Retrieve by ID");
        System.out.println("        3. Export an ID");
        System.out.println("        4. Change the master password");
        System.out.println("        5. Exit");
        System.out.print("\nPlease select a number between 1 and 5: ");

        choice = new String(GetText());

        while (!choiceList.contains(choice)){
            System.out.print("\nPlease select a number between 1 and 5: ");
            choice = new String(GetText());
        }
        return choice;
    }

    static void CheckPass(char[] mainID, String str, int count) throws NoSuchAlgorithmException, IOException {
        if (!Arrays.equals(Crypto.Hash_Encode(str, "SHA-256"), FromFile.Get_Master_Pass(mainID))) {
            System.out.println("You've entered wrong password " + count + " times out of 5 attempts.\n");
            if (count == 5) System.exit(0);
            else CheckPass(mainID, str, count + 1);
        }
    }

    static void PrintResult(char[] mainID, char[] id) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        char[] cArr = (new String(Crypto.Decrypt_AES(mainID, id, Crypto.Decode(FromFile.File_To_Map(mainID).get(new String(id)).toCharArray())))).toCharArray();
        int idx = 0;
        for (int i = 0; i < cArr.length; i++){
            if (cArr[i] == ' ') idx = i;
        }
        System.out.println("ID: " + new String(id));
        System.out.println("Username: " + (new String(cArr)).substring(0, idx));
        System.out.println("Password: " + (new String(cArr)).substring(idx + 1) + "\n");
        cArr = null;
    }
}
