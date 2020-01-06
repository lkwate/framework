package dataDefinition;

import java.util.Comparator;

public class RowComparator implements Comparator<Row>{

	private int indexColumn;
	
	public RowComparator(int indexColumn) {
		// TODO Auto-generated constructor stub
		this.indexColumn = indexColumn;
	}
	@Override
	public int compare(Row o1, Row o2) {
		// TODO Auto-generated method stub
		return o1.getStringValue(this.indexColumn).compareToIgnoreCase(o2.getStringValue(this.indexColumn));
	}

}
