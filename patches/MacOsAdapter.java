package com.cburch.logisim.gui.start;

import java.awt.Desktop;
import java.awt.Frame;
import java.awt.desktop.AppReopenedEvent;
import java.awt.desktop.AppReopenedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.roydesign.event.ApplicationEvent;
import net.roydesign.mac.MRJAdapter;

import com.cburch.logisim.gui.prefs.PreferencesFrame;
import com.cburch.logisim.proj.ProjectActions;

class MacOsAdapter {

	private static class MyListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ApplicationEvent event2 = (ApplicationEvent) event;
			int type = event2.getType();

			switch (type) {
			case ApplicationEvent.ABOUT:
				About.showAboutDialog(null);
				break;

			case ApplicationEvent.QUIT_APPLICATION:
				ProjectActions.doQuit();
				break;

			case ApplicationEvent.OPEN_DOCUMENT:
				Startup.doOpen(event2.getFile());
				break;

			case ApplicationEvent.PRINT_DOCUMENT:
				Startup.doPrint(event2.getFile());
				break;

			case ApplicationEvent.PREFERENCES:
				PreferencesFrame.showPreferences();
				break;
			}
		}
	}

	private static class MyReopenListener implements AppReopenedListener {
		@Override
		public void appReopened(AppReopenedEvent event) {
			Frame[] frames = Frame.getFrames();

			boolean anyVisible = false;

			for (Frame f : frames) {
				if (f.isVisible()) {
					anyVisible = true;
					f.setState(Frame.NORMAL);
					f.toFront();
					f.requestFocus();
				}
			}

			if (!anyVisible) {
				Startup.doNew();
			}
		}
	}

	static void addListeners(boolean added) {
		MyListener myListener = new MyListener();

		if (!added) MRJAdapter.addOpenDocumentListener(myListener);
		if (!added) MRJAdapter.addPrintDocumentListener(myListener);

		MRJAdapter.addPreferencesListener(myListener);
		MRJAdapter.addQuitApplicationListener(myListener);
		MRJAdapter.addAboutListener(myListener);

		if (!added && Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.APP_EVENT_REOPENED)) {
				desktop.addAppEventListener(new MyReopenListener());
			}
		}
	}

	public static void register() {
	}
}