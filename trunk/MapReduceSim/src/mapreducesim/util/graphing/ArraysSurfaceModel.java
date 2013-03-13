package mapreducesim.util.graphing;

import org.sf.surfaceplot.ISurfacePlotModel;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class ArraysSurfaceModel implements ISurfacePlotModel {
	public static final int X_INDEX = 0;
	public static final int Y_INDEX = 1;
	public static final int Z_INDEX = 2;
	private float[] mins;
	private float[] maxes;
	private double[][] values;
	private String[] labels;
	private double[] divisions;
	private double[] offsets;

	public ArraysSurfaceModel(double[][] graphValues, double[] offsets, double divisions[], String[] labels) {
		this.offsets = offsets;
		this.divisions = divisions;
		this.labels = labels;
		values = graphValues;
		mins = new float[3];
		maxes = new float[3];
		mins[0] = (float) offsets[0];
		mins[1] = (float) offsets[1];
		maxes[0] = (float) (offsets[0] + divisions[0] * graphValues.length);
		maxes[1] = (float) (offsets[1] + divisions[1] * graphValues[0].length);

		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for (int x = 0; x < graphValues.length; x++) {
			for (double val : graphValues[x]) {
				if ((float) val < min)
					min = (float) val;
				if ((float) val > max)
					max = (float) val;
			}
		}

		mins[2] = min;
		maxes[2] = max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sf.surfaceplot.ISurfacePlotModel#calculateZ(float, float)
	 */
	@Override
	public float calculateZ(float x, float y) {
		return (float) values[(int) ((x - offsets[X_INDEX]) / divisions[X_INDEX])][(int) ((y - offsets[Y_INDEX]) / divisions[Y_INDEX])];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sf.surfaceplot.ISurfacePlotModel#getCalcDivisions()
	 */
	@Override
	public int getCalcDivisions() {
		return (int) divisions[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sf.surfaceplot.ISurfacePlotModel#getDispDivisions()
	 */
	@Override
	public int getDispDivisions() {
		return (int) divisions[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sf.surfaceplot.ISurfacePlotModel#getPlotMode()
	 */
	@Override
	public int getPlotMode() {
		// TODO Auto-generated method stub
		return ISurfacePlotModel.PLOT_MODE_SPECTRUM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sf.surfaceplot.ISurfacePlotModel#getXAxisLabel()
	 */
	@Override
	public String getXAxisLabel() {
		return labels[X_INDEX];
	}

	@Override
	public float getXMax() {
		return maxes[X_INDEX];
	}

	@Override
	public float getXMin() {
		return mins[X_INDEX];
	}

	@Override
	public String getYAxisLabel() {
		return labels[Y_INDEX];
	}

	@Override
	public float getYMax() {
		return maxes[Y_INDEX];
	}

	@Override
	public float getYMin() {
		return mins[Y_INDEX];
	}

	@Override
	public String getZAxisLabel() {
		return labels[Z_INDEX];
	}

	@Override
	public float getZMax() {
		return maxes[Z_INDEX];
	}

	@Override
	public float getZMin() {
		return mins[Z_INDEX];
	}

	@Override
	public boolean isBoxed() {
		return true;
	}

	@Override
	public boolean isDisplayGrids() {
		return false;
	}

	@Override
	public boolean isDisplayXY() {
		return true;
	}

	@Override
	public boolean isDisplayZ() {
		return true;
	}

	@Override
	public boolean isMesh() {
		return true;
	}

	@Override
	public boolean isScaleBox() {
		return false;
	}

}
