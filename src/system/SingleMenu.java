package system;

import java.util.LinkedHashMap;

public class SingleMenu {
    private String code = new String();
    private String nameMenu = new String();
    private String codeParent = new String();
    private String tableName;
    private LinkedHashMap<String, SingleMenu> childMenus = new LinkedHashMap<>();

    public SingleMenu(String code, String codeParent, String tableName, String nameMenu) {
        if (code == null) throw new IllegalArgumentException();
        this.code = code;
        this.tableName = tableName;
        this.nameMenu = nameMenu;
        this.codeParent = codeParent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addChildMenu(SingleMenu singleMenu) {
        if (singleMenu == null) throw new IllegalArgumentException();
        this.childMenus.put(singleMenu.getCode(), singleMenu);
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public boolean isLeaf() {
        return this.childMenus.isEmpty();
    }

    public String getCodeParent() {
        return codeParent;
    }

    public void setCodeParent(String codeParent) {
        this.codeParent = codeParent;
    }

    public LinkedHashMap<String, SingleMenu> getChildMenus() {
        return childMenus;
    }
}
