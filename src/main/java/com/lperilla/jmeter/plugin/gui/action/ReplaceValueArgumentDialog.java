package com.lperilla.jmeter.plugin.gui.action;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.gui.ComponentUtil;

import com.lperilla.jmeter.plugin.gui.component.JLabeledTextField;
import com.lperilla.jmeter.plugin.util.MsgKey;
import com.lperilla.jmeter.plugin.util.Utilities;

/**
 * 
 * @author lperilla
 *
 */
public class ReplaceValueArgumentDialog extends JDialog implements ActionListener {

	private static final String ESCAPE = "ESCAPE";

	private static final long serialVersionUID = -8778440851658072425L;

	private JButton searchButton;

	private JLabeledTextField nameParameterTF;

	private JLabeledTextField valueParameterTF;

	private JCheckBox isMarkNodesCB;

	private JButton cancelButton;

	public ReplaceValueArgumentDialog() {
		super((JFrame) null, Utilities.getResString(MsgKey.TITLE_FIND_PARAMETER), true);
		this.init();
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();

		Action escapeAction = new AbstractAction(ESCAPE) {

			private static final long serialVersionUID = -265566679237921289L;

			public void actionPerformed(ActionEvent actionEvent) {
				close();
			}
		};

		ActionMap actionMap = rootPane.getActionMap();
		actionMap.put(escapeAction.getValue(Action.NAME), escapeAction);
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStrokes.ESC, escapeAction.getValue(Action.NAME));

		return rootPane;
	}

	private void init() {
		KeyAdapter adapter = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				enabledSearchButton();
			}
		};

		this.enabledSearchButton();
		this.getNameParameterTF().addKeyListener(adapter);
		this.getValueParameterTF().addKeyListener(adapter);

		this.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(BorderFactory.createEmptyBorder(7, 5, 5, 5));

		content.add(this.getNameParameterTF());
		content.add(this.getValueParameterTF());
		content.add(this.getIsMarkNodesCB());

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.add(this.getSearchButton());
		buttonsPanel.add(this.getCancelButton());

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		this.pack();
		ComponentUtil.centerComponentInWindow(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.getCancelButton()) {
			this.getNameParameterTF().requestFocusInWindow();
			this.setVisible(false);
			return;
		}
		this.doSearch(e);
	}

	public JButton getSearchButton() {
		if (searchButton == null) {
			this.searchButton = new JButton(Utilities.getResString(MsgKey.TEXT_BUTTON_FIND));
			this.searchButton.addActionListener(this);
		}
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}

	public JLabeledTextField getNameParameterTF() {
		if (this.nameParameterTF == null) {
			this.nameParameterTF = new JLabeledTextField(Utilities.getResString(MsgKey.FIELD_NAME_PARAMETER), 20);
		}
		return nameParameterTF;
	}

	public void setNameParameterTF(JLabeledTextField nameParameterTF) {
		this.nameParameterTF = nameParameterTF;
	}

	public JButton getCancelButton() {
		if (this.cancelButton == null) {
			this.cancelButton = new JButton(Utilities.getResString(MsgKey.TEXT_BUTTON_CANCEL));
			this.cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public JLabeledTextField getValueParameterTF() {
		if (this.valueParameterTF == null) {
			this.valueParameterTF = new JLabeledTextField(Utilities.getResString(MsgKey.FIELD_VALUE_PARAMETER), 20);
		}
		return valueParameterTF;
	}

	public void setValueParameterTF(JLabeledTextField valueParameterTF) {
		this.valueParameterTF = valueParameterTF;
	}

	public JCheckBox getIsMarkNodesCB() {
		if (this.isMarkNodesCB == null) {
			this.isMarkNodesCB = new JCheckBox(Utilities.getResString(MsgKey.FIELD_MARK_NODES), false);
		}
		return isMarkNodesCB;
	}

	public void setIsMarkNodesCB(JCheckBox isMarkNodesCB) {
		this.isMarkNodesCB = isMarkNodesCB;
	}

	private void doSearch(ActionEvent e) {
		List<HTTPArgument> arguments = new LinkedList<HTTPArgument>();
		List<JMeterTreeNode> nodes = new LinkedList<JMeterTreeNode>();

		String nameParam = getNameParameterTF().getText();

		GuiPackage guiPackage = GuiPackage.getInstance();

		JMeterTreeModel jMeterTreeModel = guiPackage.getTreeModel();
		for (JMeterTreeNode nodeSampler : jMeterTreeModel.getNodesOfType(HTTPSamplerProxy.class)) {
			
			HTTPSamplerProxy testElement = (HTTPSamplerProxy) nodeSampler.getUserObject();
			PropertyIterator propertiesIter = testElement.getArguments().iterator();

			while (propertiesIter.hasNext()) {

				HTTPArgument httpArgument = null;
				Object objectValue = propertiesIter.next().getObjectValue();
				
				try {
					httpArgument = (HTTPArgument) objectValue;
				} catch (ClassCastException ex) {
					httpArgument = new HTTPArgument((Argument) objectValue);
				}
				
				if (httpArgument.getName().equals(nameParam)) {
					arguments.add(httpArgument);
					nodes.addAll(nodeSampler.getPathToThreadGroup());
				}
			}
		}
		
		if (arguments.isEmpty()) {
			JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), 
					Utilities.getResString(MsgKey.MESSAGE_NO_FIND_PARAMETER, nameParam),
					Utilities.getResString(MsgKey.TITLE_FIND_PARAMETER), JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int response = JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), 
				Utilities.getResString(MsgKey.MESSAGE_FIND_PARAMETER, arguments.size()),
				Utilities.getResString(MsgKey.TITLE_FIND_PARAMETER), 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (response == JOptionPane.YES_OPTION) {
			ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SEARCH_RESET));
			
			this.setValueArguments(arguments);
			
			if (this.getIsMarkNodesCB().isSelected())
				this.markNodeTree(nodes);

			this.close();
		}
	}

	/**
	 * Metodo que habilita o deshabilitar
	 */
	private void enabledSearchButton() {
		if (this.getNameParameterTF().getText().trim().length() > 0 && this.getValueParameterTF().getText().trim().length() > 0) {
			this.getSearchButton().setEnabled(true);
		} else {
			this.getSearchButton().setEnabled(false);
		}
	}

	private void markNodeTree(List<JMeterTreeNode> nodes) {
		GuiPackage guiInstance = GuiPackage.getInstance();
		JTree jTree = guiInstance.getMainFrame().getTree();

		for (JMeterTreeNode iterator : nodes) {
			iterator.setMarkedBySearch(true);
			jTree.expandPath(new TreePath(iterator.getPath()));
		}
	}

	private void setValueArguments(List<HTTPArgument> arguments) {
		String valueParam = this.getValueParameterTF().getText();
		for (HTTPArgument arg : arguments) {
			arg.setValue(valueParam);
		}
	}

	private void close() {
		GuiPackage.getInstance().getMainFrame().repaint();
		this.getNameParameterTF().requestFocusInWindow();
		this.setVisible(false);
	}

}
