package manageMomento;

public class Momento<T> {

    private T object;

    protected Momento(T object) {
        this.object = object;
    }
    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
