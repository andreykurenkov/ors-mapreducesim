package mapreducesim.util.graphing;

import java.awt.BorderLayout;

import org.sf.surfaceplot.ISurfacePlotModel;
import org.sf.surfaceplot.SurfaceCanvas;

/**
 * 
 * @author siva
 */
public class GraphingFrame extends javax.swing.JFrame {

	/** Creates new form Example */
	public GraphingFrame(ISurfacePlotModel model) {
		initComponents();

		setSize(1000, 800);
		SurfaceCanvas canvas = new SurfaceCanvas();
		canvas.setModel(model);
		centerPanel.add(canvas, BorderLayout.CENTER);
		canvas.repaint();
		setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		centerPanel = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		centerPanel.setLayout(new java.awt.BorderLayout());

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

		jLabel1.setText("Rotate: Mouse Click & Drag");
		jPanel1.add(jLabel1);

		jLabel2.setText("Zoom: Shift Key + Mouse Click & Drag");
		jPanel1.add(jLabel2);

		jLabel3.setText("Move: Control Key + Mouse Click & Drag");
		jPanel1.add(jLabel3);

		centerPanel.add(jPanel1, java.awt.BorderLayout.PAGE_END);

		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel centerPanel;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	// End of variables declaration//GEN-END:variables

}