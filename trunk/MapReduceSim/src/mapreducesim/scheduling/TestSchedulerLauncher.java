package mapreducesim.scheduling;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class TestSchedulerLauncher {

	
	public static void main(String[] args) throws NativeException, IOException, InterruptedException{
		launch("tests/platform.xml","tests/schedulertest/depl.xml");
	}
	
	/* This only contains the launcher. If you do nothing more than than you can run 
	    *   java simgrid.msg.Msg
	    * which also contains such a launcher
	    */
	    
	    public static void launch(String platformFilePath,String deploymentFilePath) throws NativeException, IOException, InterruptedException {    	
	    	
	    	
	    	
	    	
	    	
			/* initialize the MSG simulation. Must be done before anything else (even logging). */
			Msg.init(new String[]{platformFilePath,deploymentFilePath});
			
			/* construct the platform and deploy the application */
			Msg.createEnvironment(platformFilePath);
			Msg.deployApplication(deploymentFilePath);
			
			/*  execute the simulation. */
		    Msg.run();
	    }
	
}
