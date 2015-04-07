/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lperilla.jmeter.plugin.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author lperilla
 *
 */
public class JLabeledTextField extends JPanel implements JLabeledField, FocusListener {

	private static final long serialVersionUID = 240L;

	private JLabel label;

	private JTextField textField;

	// Maybe move to vector if MT problems occur
	private final ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>(3);

	// A temporary cache for the focus listener
	private String oldValue = "";

	/**
	 * Default constructor, The label and the Text field are left empty.
	 */
	public JLabeledTextField() {
		this("", 20);
	}

	/**
	 * Constructs a new component with the label displaying the passed text.
	 *
	 * @param pLabel
	 *            The text to in the label.
	 */
	public JLabeledTextField(String pLabel) {
		this(pLabel, 20);
	}

	public JLabeledTextField(String pLabel, int size) {
		super();
		setTextField(createTextField(size));
		setLabel(new JLabel(pLabel));
		getLabel().setLabelFor(getTextField());
		init();
	}

	public JLabeledTextField(String pLabel, Color bk) {
		super();
		setTextField(createTextField(20));
		setLabel(new JLabel(pLabel));
		getLabel().setBackground(bk);
		getLabel().setLabelFor(getTextField());
		this.setBackground(bk);
		init();
	}

	/**
	 * Get the label {@link JLabel} followed by the text field @link
	 * {@link JTextField}.
	 */
	public List<JComponent> getComponentList() {
		List<JComponent> comps = new LinkedList<JComponent>();
		comps.add(getLabel());
		comps.add(getTextField());
		return comps;
	}

	/** {@inheritDoc} */
	@Override
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		getTextField().setEnabled(enable);
	}

	protected JTextField createTextField(int size) {
		return new JTextField(size);
	}

	/**
	 * Initialises all of the components on this panel.
	 */
	private void init() {
		this.setLayout(new BorderLayout(5, 0));
		// Register the handler for focus listening. This handler will
		// only notify the registered when the text changes from when
		// the focus is gained to when it is lost.
		this.getTextField().addFocusListener(this);

		// Add the sub components
		this.add(this.getLabel(), BorderLayout.WEST);
		this.add(this.getTextField(), BorderLayout.CENTER);
	}

	/**
	 * Callback method when the focus to the Text Field component is lost.
	 *
	 * @param pFocusEvent
	 *            The focus event that occured.
	 */
	public void focusLost(FocusEvent pFocusEvent) {
		// Compare if the value has changed, since we received focus.
		if (!oldValue.equals(getTextField().getText())) {
			notifyChangeListeners();
		}
	}

	/**
	 * Catch what the value was when focus was gained.
	 */
	public void focusGained(FocusEvent pFocusEvent) {
		oldValue = this.getTextField().getText();
	}

	/**
	 * Set the text displayed in the label.
	 *
	 * @param pLabel
	 *            The new label text.
	 */
	public void setTextLabel(String pLabel) {
		getLabel().setText(pLabel);
	}

	/**
	 * Set the text displayed in the Text Field.
	 *
	 * @param text
	 *            The new text to display in the text field.
	 */
	public void setText(String text) {
		getTextField().setText(text);
	}

	/**
	 * Returns the text in the Text Field.
	 *
	 * @return The text in the Text Field.
	 */
	public String getText() {
		return getTextField().getText();
	}

	/**
	 * Returns the text of the label.
	 *
	 * @return The text of the label.
	 */
	public String getTextLabel() {
		return getLabel().getText();
	}

	/**
	 * Registers the text to display in a tool tip. The text displays when the
	 * cursor lingers over the component.
	 * 
	 * @param text
	 *            the string to display; if the text is null, the tool tip is
	 *            turned off for this component
	 */
	public void setToolTipText(String text) {
		this.getLabel().setToolTipText(text);
		this.getTextField().setToolTipText(text);
	}

	/**
	 * Returns the tooltip string that has been set with setToolTipText
	 * 
	 * @return the text of the tool tip
	 */
	public String getToolTipText() {
		if (getTextField() == null) { // Necessary to avoid NPE when testing serialisation
			return null;
		}
		return getTextField().getToolTipText();
	}

	/**
	 * Adds a change listener, that will be notified when the text in the text
	 * field is changed. The ChangeEvent that will be passed to registered
	 * listeners will contain this object as the source, allowing the new text
	 * to be extracted using the {@link #getText() getText} method.
	 *
	 * @param pChangeListener
	 *            The listener to add
	 */
	public void addChangeListener(ChangeListener pChangeListener) {
		changeListeners.add(pChangeListener);
	}

	/**
	 * Removes a change listener.
	 *
	 * @param pChangeListener
	 *            The change listener to remove.
	 */
	public void removeChangeListener(ChangeListener pChangeListener) {
		changeListeners.remove(pChangeListener);
	}

	/**
	 * Notify all registered change listeners that the text in the text field
	 * has changed.
	 */
	protected void notifyChangeListeners() {
		ChangeEvent ce = new ChangeEvent(this);
		for (int index = 0; index < changeListeners.size(); index++) {
			changeListeners.get(index).stateChanged(ce);
		}
	}
	
	@Override
    public void addKeyListener(KeyListener l) {
        if (l == null) return;
        this.getTextField().addKeyListener(l);
    }

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}
	
}
