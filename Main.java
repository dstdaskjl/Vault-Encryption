// @author Mina Otgonbold
// @author Seongmin An

class Main {

    public static void main(String[] args) {
        char[] mainID;

        try{
            mainID = Initialization.Ask_UserInfo();
            while (true) {
                String choice = Function.Ask_Choice();
                if (!choice.equals("5")) Function.CheckPass(mainID, "Master Password", 1);
                switch (choice) {
                    case "1":
                        Action.Store(mainID);
                        break;
                    case "2":
                        Action.Retrieve(mainID);
                        break;
                    case "3":
                        Action.Export(mainID);
                        break;
                    case "4":
                        Action.Change_Master_Password(mainID);
                        break;
                    case "5":
                        System.out.println("\nYou are successfully logged out!\n");
                        mainID = null;
                        System.exit(0);
                        break;
                }
            }
        }
        catch(Exception e){
            mainID = null;
            System.out.println("There was an error.\n");
        }
    }
}
