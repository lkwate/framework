package systemGui;

import exceptions.WrongContainsOfFile;

public class GuiDataAdministrator {

    private static final String PATHTOFORM = "./forms/login.txt";


    public static TemplateDataForLogin loadTemplateDataLoginForm() throws WrongContainsOfFile {
        return new TemplateDataForLogin(GuiDataAdministrator.PATHTOFORM);
    }
}
