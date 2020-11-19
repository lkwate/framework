package system;

import dataDefinition.Row;
import dataDefinition.SpreadSheet;

import java.util.*;

/**
 * @author kwate
 * goal: this class define data strcuture and algorithm to load conveniently data of menu required to graphic user interface
 */
public class MenuTree {
    private LinkedHashMap<String, SingleMenu> listAllMenu = new LinkedHashMap<>();
    private List<SingleMenu> rootMenu = new LinkedList<SingleMenu>();

    public MenuTree(SpreadSheet spreadSheet) {
        if (spreadSheet == null) throw new IllegalArgumentException();
        SingleMenu singleMenu;
        int indexCode = spreadSheet.getIndexColumnName().getValueByKey("code");
        int indexTableName = spreadSheet.getIndexColumnName().getValueByKey("tableName");
        int indexNameMenu = spreadSheet.getIndexColumnName().getValueByKey("nom_rubrique");
        int indexCodeParent = spreadSheet.getIndexColumnName().getValueByKey("code_parent");
        /**
         * this is attribute is necessary when the data provided is not ordered
         */
        Queue<SingleMenu> queue = new LinkedList<>();
        /**
         * loading data from spreadsheet
         */
        for (Row row : spreadSheet.getRows()) {
            singleMenu = new SingleMenu(row.getStringValue(indexCode), row.getStringValue(indexCodeParent), row.getStringValue(indexTableName), row.getStringValue(indexNameMenu));
            this.listAllMenu.put(singleMenu.getCode(), singleMenu);
            if (singleMenu.getCodeParent() == null) {
                this.rootMenu.add(singleMenu);
            }
            else {
                if (!this.listAllMenu.containsKey(singleMenu.getCodeParent())) {
                    /**
                     * put singleMenu in a stack
                     */
                    queue.add(singleMenu);
                }
                else {
                    this.listAllMenu.get(singleMenu.getCodeParent()).addChildMenu(singleMenu);
                }
            }
        }
        if (!queue.isEmpty()) {
            /**
             * is queue is empty it means that there are singleMenu do not exit in data ready for GUI
             */
            while(!queue.isEmpty()) {
                singleMenu = queue.poll();
                if (!this.listAllMenu.containsKey(singleMenu.getCodeParent())) {
                    /**
                     * put singleMenu in a stack
                     */
                    queue.add(singleMenu);
                }
                else {
                    this.listAllMenu.get(singleMenu.getCodeParent()).addChildMenu(singleMenu);
                }
            }
        }
        this.listAllMenu.clear();
    }

    public LinkedHashMap<String, SingleMenu> getListAllMenu() {
        return listAllMenu;
    }

    public List<SingleMenu> getRootMenu() {
        return rootMenu;
    }

    public void clear() {
        this.listAllMenu.clear();
        this.rootMenu.clear();
    }
}
