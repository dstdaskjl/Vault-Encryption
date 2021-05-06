import javax.xml.bind.DatatypeConverter;

public class Action {
    static void Store(char[] mainID) {
        char[] id;

        try{
            id = Function.GetText("ID");
            if (FromFile.Does_ID_Exist(mainID, id)){
                System.out.println("The ID is already in the vault.");
                return;
            }

            byte[] encrypted;
            char[] username = Function.GetText("username");
            System.out.print("Would you like to use a random password? (Y/N): ");
            if (Function.Ask_Yes_No()){
                encrypted = Crypto.Encrypt_AES(mainID, id, username, Crypto.Get_Random_Pass());
            }
            else{
                encrypted = Crypto.Encrypt_AES(mainID, id, username, Function.GetPass("password"));
            }
            FromFile.Write_To_File_Append(mainID, new String(id) + " " + new String(Crypto.Encode(encrypted)));
            System.out.println("Your new information has been saved.\n");

            id = null;
            username = null;
        }
        catch(Exception e){
            id = null;
            System.out.println("There was an error storing new information.\n");
        }
    }


    static void Retrieve(char[] mainID) {
        char[] id;
        try{
            id = Function.GetText("ID");
            if (!FromFile.Does_ID_Exist(mainID, id)){
                System.out.println("There is no such ID.\n");
                return;
            }
            Function.PrintResult(mainID, id);
            id = null;
        }
        catch (Exception e){
            id = null;
            System.out.println("Error\n");
        }
    }
    static void Export(char[] mainID) {
        char[] id;
        try{
            id = Function.GetText("ID");
            if (!FromFile.Does_ID_Exist(mainID, id)){
                System.out.println("There is no such ID.\n");
                return;
            }

            FromFile.Write_To_File_Overwrite(id, DatatypeConverter.printBase64Binary(Crypto.Encrypt_RSA(mainID, id, Function.GetText("name of certificate"))));
            id = null;
        }
        catch (Exception e){
            id = null;
        }
    }

    static void Change_Master_Password(char[] mainID) {
        try {
            FromFile.Write_To_File_Overwrite(mainID, FromFile.File_To_List(mainID), new String(Crypto.Hash_Encode("new Master Password", "SHA-256")));
            System.out.println("Your master password has been changed.\n");
        }
        catch (Exception e){
            System.out.println("There was an error changing password .\n");
        }
    }
}
