package mapreducesim.execution;

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

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.KeyValuePair;
import mapreducesim.storage.StorageProcess;
import mapreducesim.util.xml.XMLElement;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public class SimpleShuffleSorter extends ShuffleSorter {
	private final double CONVERSION;

	/**
	 * @param input
	 */
	public SimpleShuffleSorter(XMLElement input) {
		super(input);
		CONVERSION = 5;// TODO:get from input
	}

	public SimpleShuffleSorter() {
		super(null);
		CONVERSION = 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.ShuffleSorter#doShuffleSort(mapreducesim.scheduling.FileSplitter.InputSplit)
	 */
	@Override
	public Map<String, List<KeyValuePair>> doShuffleSort(InputSplit split, WorkerProcess process)
			throws TransferFailureException, HostFailureException, TimeoutException {
		HashMap<String, List<KeyValuePair>> map = new HashMap<String, List<KeyValuePair>>();
		for (DataLocation location : split.getLocations()) {
			ReadRequestTask task = new ReadRequestTask(location, process.MAILBOX);
			task.send(StorageProcess.STORAGE_MAILBOX);
			Task response = null;
			do {
				response = Task.receive(process.MAILBOX);
			} while (!(response instanceof FileTransferTask));
			List<FileBlock> blocks = ((FileTransferTask) response).getTransferData();
			for (FileBlock block : blocks) {
				for (KeyValuePair pair : block.getPairs()) {
					if (!map.containsKey(pair.getKey()))
						map.put(pair.getKey(), new LinkedList<KeyValuePair>());
					map.get(pair.getKey()).add(pair);
				}

				process.waitFor(block.getSize() / process.getHost().getSpeed() * CONVERSION);
			}
		}
		return map;
	}

}
