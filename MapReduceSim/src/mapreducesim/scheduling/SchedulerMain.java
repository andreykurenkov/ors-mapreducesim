package mapreducesim.scheduling;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class SchedulerMain {

	/* This only contains the launcher. If you do nothing more than than you can run 
	    *   java simgrid.msg.Msg
	    * which also contains such a launcher
	    */
	    
	    public static void main(String[] args) throws NativeException, IOException, InterruptedException {    	
	    	
	    	
	    	
	    	
	    	
			/* initialize the MSG simulation. Must be done before anything else (even logging). */
			Msg.init(args);
			
			if(args.length < 2) {
				Msg.info("Usage   : program platform_file deployment_file");
		    	Msg.info("example : program ping_pong_platform.xml ping_pong_deployment.xml");
		    	System.exit(1);
		    	
		    	}
		
			/* construct the platform and deploy the application */
			Msg.createEnvironment(args[0]);
			Msg.deployApplication(args[1]);
			
			/*  execute the simulation. */
		    Msg.run();
	    }
	
}
