package com.lperilla.jmeter.plugin.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.jmeter.gui.action.Command;

/**
 * 
 * @author lperilla
 *
 */
public class ReplaceValueArgumentCommand implements Command {

	private static final Set<String> commands = new HashSet<String>();

	private ReplaceValueArgumentDialog parameterDialog;

	static {
		commands.add(ActionNames.REPLACE_PARAMETER);
	}

	public Set<String> getActionNames() {
		return commands;
	}

	public void doAction(ActionEvent e) {
		this.getParameterDialog().setVisible(true);
	}

	public ReplaceValueArgumentDialog getParameterDialog() {
		if (parameterDialog == null) {
			parameterDialog = new ReplaceValueArgumentDialog();
		}
		return parameterDialog;
	}

	public void setParameterDialog(ReplaceValueArgumentDialog parameterDialog) {
		this.parameterDialog = parameterDialog;
	}

}
