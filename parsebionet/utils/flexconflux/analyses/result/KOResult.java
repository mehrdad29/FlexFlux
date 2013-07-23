/*******************************************************************************
 * Copyright INRA
 * 
 *  Contact: ludovic.cottret@toulouse.inra.fr
 * 
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *  In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *  The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 ******************************************************************************/
/**
 * 15 mars 2013 
 */
package parsebionet.utils.flexconflux.analyses.result;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.ui.RefineryUtilities;

import parsebionet.biodata.BioEntity;
import parsebionet.utils.flexconflux.Vars;

/**
 * 
 * Class representing the result of a KO analysis.
 * 
 * @author lmarmiesse 15 mars 2013
 * 
 */
public class KOResult extends AnalysisResult {

	/**
	 * Map containing the result for each knocked out entity.
	 */
	private Map<BioEntity, Double> map = new HashMap<BioEntity, Double>();

	/**
	 * Field to search for entities in the plot.
	 */
	private JTextField searchField;

	/**
	 * Table with all results.
	 */
	private JTable resultTable = new JTable(0, 2);

	/**
	 * Adds a value to the map.
	 */
	public synchronized void addLine(BioEntity entity, double value) {

		map.put(entity, value);
	}

	public void writeToFile(String path) {
		try {
			PrintWriter out = new PrintWriter(new File(path));

			out.println("KO results : \n");

			for (BioEntity entity : map.keySet()) {

				out.println(entity.getId() + " obj value : "
						+ Vars.round(map.get(entity)));
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double getValueForEntity(BioEntity ent) {
		return map.get(ent);
	}

	public void plot() {

		String[] columnNames = { "Entity name", "Objective value" };

		Object[][] data = new Object[map.size()][columnNames.length];

		int i = 0;
		for (BioEntity ent : map.keySet()) {

			data[i] = new Object[] { ent.getId(), Vars.round(map.get(ent)) };
			i++;
		}

		DefaultTableModel model = new MyTableModel(data, columnNames);
		resultTable.setModel(model);

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				resultTable.getModel());
		resultTable.setRowSorter(sorter);

		JPanel northPanel = new JPanel();

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

		JPanel searchPanel = new JPanel(new FlowLayout());

		searchPanel.add(new JLabel("Search for an entity : "));

		searchField = new JTextField(10);
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
				updateTable(sorter);
			}

			public void insertUpdate(DocumentEvent arg0) {
				updateTable(sorter);
			}

			public void removeUpdate(DocumentEvent arg0) {
				updateTable(sorter);
			}

		});

		searchPanel.add(searchField);

		centerPanel.add(searchPanel);
		centerPanel.add(new JScrollPane(resultTable));

		JFrame frame = new JFrame("KO results");

		frame.add(northPanel, BorderLayout.PAGE_START);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	/**
	 * Updates the table when a search is made in the plot.
	 */
	private void updateTable(TableRowSorter<TableModel> sorter) {
		String text = searchField.getText();
		if (sorter.getModelRowCount() != 0 && text.length() != 0) {
			// case insensitive
			sorter.setRowFilter(RowFilter.regexFilter(
					"(?i)" + Pattern.quote(text), 0));
		}

	}

}
