package mapreducesim.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import mapreducesim.core.SimConfig;
import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.storage.StorageProcess;
import mapreducesim.util.xml.XMLElement;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public class SimpleShuffleSorter extends ShuffleSorter {
	private double conversion;
	private double percentSameKey;

	/**
	 * @param input
	 */
	public SimpleShuffleSorter(XMLElement input) {
		super(input);
		conversion = SimConfig.parseDoubleAttribute(input, "conversion", 5);
		percentSameKey = SimConfig.parseDoubleAttribute(input, "percentSameKey", 0.1);
	}

	public SimpleShuffleSorter() {
		super(null);
		conversion = 5;
		percentSameKey = 0.1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.ShuffleSorter#doShuffleSort(mapreducesim.scheduling.FileSplitter.InputSplit)
	 */
	@Override
	public List<KeyValuePairs> doShuffleSort(InputSplit split, WorkerProcess process) throws TransferFailureException,
			HostFailureException, TimeoutException {
		ArrayList<KeyValuePairs> pairs = new ArrayList<KeyValuePairs>();
		for (DataLocation location : split.getLocations()) {
			ReadRequestTask task = new ReadRequestTask(location, process.MAILBOX);
			task.send(StorageProcess.STORAGE_MAILBOX);
			Task response = null;
			do {
				response = Task.receive(process.MAILBOX);
			} while (!(response instanceof FileTransferTask));
			List<FileBlock> blocks = ((FileTransferTask) response).getTransferData();
			for (FileBlock block : blocks) {
				for (KeyValuePairs pair : block.getPairs()) {
					pairs.add(pair);
				}
				// NlogN type stuff
				process.waitFor(block.getSize() / process.getHost().getSpeed() * conversion);
			}
		}
		return pairs;
	}

}
