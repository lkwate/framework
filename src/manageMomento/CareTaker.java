package manageMomento;

public class CareTaker<T> {

    private Momento<T> momento;

    public void save (Momento<T> momento) {
        this.momento = momento;
    }

    public Momento<T> retrieve() {
        return this.momento;
    }
}
