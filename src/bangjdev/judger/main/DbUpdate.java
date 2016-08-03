package bangjdev.judger.main;

import java.io.File;
import java.util.Vector;

public class DbUpdate {
	public Vector<String> loadTest(File file) {
		Vector<String> result = new Vector<>();
		result.clear();
		result.add("Contestants");
		for (File x : file.listFiles()) {
			if (x.isDirectory())
				result.add(x.getName());
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Vector<Vector> loadContestants(File file) {
		Vector<Vector> result = new Vector<>();
		Vector<String> row;
		for (File x : file.listFiles())
			if (x.isDirectory()) {
				row = new Vector<>();
				row.add(x.getName());
				result.add(row);
			}
		return result;
	}
}
