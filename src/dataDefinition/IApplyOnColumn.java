package dataDefinition;

@FunctionalInterface
public interface IApplyOnColumn {

	String apply(String functionName, String value);
}
