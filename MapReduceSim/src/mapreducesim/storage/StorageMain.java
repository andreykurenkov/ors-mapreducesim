package mapreducesim.storage;

import java.io.IOException;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class StorageMain {

	public static void main(String[] args) throws NativeException, IOException,
			InterruptedException {

		Msg.init(args);

		if (args.length < 2) {
			Msg.info("Usage: program platform_file deployment_file");
			System.exit(1);
		}

		Msg.createEnvironment(args[0]);
		Msg.deployApplication(args[1]);

		Msg.run();
	}

}
