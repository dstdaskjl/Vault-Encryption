import java.io.*;
import java.security.NoSuchAlgorithmException;

class Initialization {
    static char[] Ask_UserInfo() throws IOException, NoSuchAlgorithmException {
        char[] id = Function.GetText("Master ID");
        if (Does_Vault_Exist(id)) {
            Function.CheckPass(id, "Master Password", 1);
            System.out.println("You are successfully logged in.\n");
        } else {
            System.out.print("We couldn't find the user. Would you like to make a new vault? (Y/N): ");
            if (Function.Ask_Yes_No()) Create_New_Vault(id);
            else System.exit(0);
        }
        return id;
    }

    static boolean Does_Vault_Exist(char[] mainID) {
        return new File(String.valueOf(mainID) + ".txt").exists();
    }

    static void Create_New_Vault(char[] mainID) throws IOException, NoSuchAlgorithmException {
        FromFile.Write_To_File_Append(mainID, String.valueOf(Crypto.Hash_Encode("Master Pass", "SHA-256")));
    }
}
