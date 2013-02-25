package mapreducesim.execution.test;

import java.util.List;

import mapreducesim.scheduling.JobSubmission;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.util.xml.XMLElement;

public abstract class FileSplitter {
	protected XMLElement input;

	public FileSplitter(XMLElement input) {
		this.input = input;
	}

	public abstract KeyValueReader getKeyValueReader(InputSplit split);

	public abstract List<InputSplit> getInputSlits(JobSubmission job);

	public static class InputSplit {
		private FileBlockLocation[] blocks;
		private int size;

	}
}
