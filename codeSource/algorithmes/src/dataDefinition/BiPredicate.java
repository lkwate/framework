package dataDefinition;

@FunctionalInterface
public interface BiPredicate<V, R> {

	boolean test(V value, R rule);
	
}
