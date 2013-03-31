package mapreducesim.scheduling.test;

import java.io.IOException;

import mapreducesim.util.SmartFile;

public class LocalityTestScriptGen {
	public static void main(String[] args) {
		StringBuilder out = new StringBuilder();
		String nl = System.getProperty("line.separator");
		for (int si = 0; si < LocalityTestJobMaker.NUM_SIMULATIONS; si++) {
			out
					.append("java -cp ./bin:/opt/simgrid-java/java/simgrid.jar mapreducesim/scheduling/test/LocalityTestJobMaker "
							+ si);
			out.append(nl);
		}
		SmartFile f = new SmartFile("ltest.sh");
		try {
			f.createNewFile();
			f.write(out.toString(), false);
			SmartFile mlm = new SmartFile("ml.m");
			mlm.createNewFile();
			mlm.write("%% gaussWidth nodeLocalPct rackLocalPct remotePct" + nl
					+ "numNodes = " + LocalityTestJobMaker.NUM_NODES + ";" + nl
					+ "data = [", false);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
