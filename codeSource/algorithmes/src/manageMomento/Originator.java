package manageMomento;

public class Originator<T> {

    private T object;
    private Momento<T> momento;

    public void setState(T object) {
        this.object = object;
    }

    public Momento<T> originatorMomento() {
        this.momento = new Momento<>(this.object);
        return this.momento;
    }

    public T revert(Momento<T> momento) {
        return momento.getObject();
    }
}
