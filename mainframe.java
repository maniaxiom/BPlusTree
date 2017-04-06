import java.io.IOException;


public class mainframe {
	public static void main(String[] args) throws IOException{
		ExternalMergeSorter ems = new ExternalMergeSorter();
		int numf = ems.readNVals(2, "dataset");
		ems.mergeFiles(numf, "finalfile");
	}
}
