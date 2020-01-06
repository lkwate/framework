package security;

/**
 * this class if use to define the right of a given user
 */
public class Droit {
    public static final String CREATE = "CREATE";
    public static final String READ = "READ";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    private String rubric = new String();
    private String libelleOperation = new String();

    @Override
    public String toString() {
        return this.rubric+";"+this.libelleOperation;
    }
}
