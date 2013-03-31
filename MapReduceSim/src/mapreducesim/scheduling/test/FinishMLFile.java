package mapreducesim.scheduling.test;

import java.io.IOException;

import mapreducesim.util.SmartFile;

public class FinishMLFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			SmartFile ml = new SmartFile("ml.m");
			String nl = System.getProperty("line.separator");
			ml.createNewFile();
			ml
					.write(
							"];"
									+ nl
									+ nl
									+ "sigma = data(:,1);"
									+ nl
									+ "nodeLocalPct = data(:,2);"
									+ nl
									+ "rackLocalPct = data(:,3);"
									+ nl
									+ "remotePct = data(:,4);"
									+ nl
									+ nl
									+ "plot(sigma,nodeLocalPct,'-sb');"
									+ nl
									+ "hold on;"
									+ nl
									+ "plot(sigma,rackLocalPct,'-dr');"
									+ nl
									+ "plot(sigma,remotePct,'-*k');"
									+ nl
									+ "xlabel('sigma');"
									+ nl
									+ "ylabel('% scheduling decisions');"
									+ nl
									+ "title(['Map Task Scheduling Decisions on a Simulated ' num2str(numNodes) '-node Cluster Using FIFO Scheduling Algorithm']);"
									+ nl
									+ "legend('nodeLocal','rackLocal','remote');"
									+ nl + "hold off;", true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
