package security;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SqlInjection {

    private static final String echapCharacter = "\\";
    private static final List<String> ListcharacterToEchap = new LinkedList<>();
    static {
        SqlInjection.ListcharacterToEchap.addAll(Arrays.asList(new String[]{";","-","d","*","/","$","(",")","!","=","%","&","~","<",">",","}));
    }

    public static String echapCharacter(String chaine) {
        if (chaine == null) return null;
        String result = chaine.toString();
        for (String c : SqlInjection.ListcharacterToEchap) {
            result =  result.replace(c, SqlInjection.echapCharacter+c);
        }
        return result;
    }

    public static String undoEchapCharacter(String chaine) {
        if (chaine == null) return null;
        String result = null;
        for (String c : SqlInjection.ListcharacterToEchap) {
            result =  chaine.replace(SqlInjection.echapCharacter+c, c);
        }
        return result;
    }

    public static void main(String[] args) {
        String text = ";;-dslfkjlkjsdfjl-";
        String textechap = SqlInjection.echapCharacter(text);
        text = SqlInjection.undoEchapCharacter(textechap);
        System.out.println(textechap+"\n"+text);
    }
}
