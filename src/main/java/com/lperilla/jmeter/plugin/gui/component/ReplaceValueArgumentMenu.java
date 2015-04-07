package com.lperilla.jmeter.plugin.gui.component;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.plugin.MenuCreator;

import com.lperilla.jmeter.plugin.gui.action.ActionNames;
import com.lperilla.jmeter.plugin.util.MsgKey;
import com.lperilla.jmeter.plugin.util.Utilities;

/**
 * 
 * @author lperilla
 *
 */
public class ReplaceValueArgumentMenu implements MenuCreator{

	private JMenu menu;

	private JMenuItem item;

	public ReplaceValueArgumentMenu() {
		super();
	}

	public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION arg0) {
		return new JMenuItem[] { this.getItem() };
	}

	public JMenu[] getTopLevelMenus() {
		return new JMenu[] { this.getMenu() };
	}

	public void localeChanged() {
	}

	public boolean localeChanged(MenuElement arg0) {
		return false;
	}

	public JMenu getMenu() {
		if (this.menu == null) {
			this.menu = new JMenu(Utilities.getResString(MsgKey.MENU_UTILITY));
			this.menu.add(this.getItem());
		}
		return menu;
	}

	public void setMenu(JMenu menu) {
		this.menu = menu;
	}

	public JMenuItem getItem() {
		if (this.item == null) {
			this.item = new JMenuItem(Utilities.getResString(MsgKey.MENU_UTILITY_REPLACE));
			item.setName(MsgKey.MENU_UTILITY_REPLACE);
			item.setActionCommand(ActionNames.REPLACE_PARAMETER);
			item.setAccelerator(null);
			item.addActionListener(ActionRouter.getInstance());
		}
		return item;
	}

	public void setItem(JMenuItem item) {
		this.item = item;
	}

}
