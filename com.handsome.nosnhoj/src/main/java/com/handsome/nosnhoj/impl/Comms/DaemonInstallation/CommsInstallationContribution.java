package com.handsome.nosnhoj.impl.Comms.DaemonInstallation;

import com.handsome.nosnhoj.impl.util.GV;
import com.handsome.nosnhoj.impl.Comms.Daemon.CommsDaemonService;
import com.handsome.nosnhoj.impl.Comms.Daemon.CommsXmlRpc;
import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.variable.GlobalVariable;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CommsInstallationContribution implements InstallationNodeContribution {
//	private static final String POPUPTITLE_KEY = "popuptitle";

	private static final String XMLRPC_VARIABLE = "dae_ard";

	public static final int PORT = 40405;
	
	private static final String KEY_ENABLED = "enabled";
	
	private static final String VAR_READ_STRING_VARIABLE = "read_string_buffer";
	private static final String FUN_GET_READ_STRING = "get_read_string";
	private static final String FUN_MSG_CHECK = "msg_is";
	
	private static final String FUN_STR_CONTAINS = "str_find";
	
	
	private final GlobalVariable g_READ_STRING_VARIABLE;

	private DataModel model;

	private final CommsInstallationView view;
	private final CommsDaemonService daemonService;
	private CommsXmlRpc xmlRpcDaemonInterface;
	private Timer uiTimer;
	private boolean pauseTimer = false;

	/*========================================================================================
     * Constructor
     * ======================================================================================*/
	public CommsInstallationContribution(InstallationAPIProvider apiProvider, CommsInstallationView view, DataModel model, CommsDaemonService daemonService, CreationContext context) {
		this.view = view;
		this.daemonService = daemonService;
		this.model = model;

		this.g_READ_STRING_VARIABLE = new GV(VAR_READ_STRING_VARIABLE);
//		util.AddScriptFunction(apiProvider, FUN_GET_READ_STRING);
//		util.AddScriptFunction(apiProvider, FUN_MSG_CHECK, "msg");
//		util.AddScriptFunction(apiProvider, FUN_STR_CONTAINS, "string", "find");
		
		xmlRpcDaemonInterface = new CommsXmlRpc("127.0.0.1", PORT);
		if (context.getNodeCreationType() == CreationContext.NodeCreationType.NEW) {
		}
		DaemonThread();
	}

	/*========================================================================================
     * Get and sets
     * ======================================================================================*/
	public boolean isDefined() {
		return getDaemonState() == DaemonContribution.State.RUNNING;
	}

	private DaemonContribution.State getDaemonState() {
		return daemonService.getDaemon().getState();
	}

	private Boolean IsDaemonEnabled() {
		return model.get(KEY_ENABLED, true); //This daemon is enabled by default
	}

	public String GetXmlRpcVariable(){
		return XMLRPC_VARIABLE;
	}

	public CommsXmlRpc GetXmlRpc() {
		return xmlRpcDaemonInterface;
	}
	
	public String GetVarReadString() {
		return g_READ_STRING_VARIABLE.getDisplayName();
	}
	public String GetFunGetReadString() {
		return FUN_GET_READ_STRING;
	}
	/*========================================================================================
     * Listener
     * ======================================================================================*/
	private void UpdateStatusUI() {
		DaemonContribution.State state = getDaemonState();

		if (state == DaemonContribution.State.RUNNING) {
			view.SetStartButtonState(false);
			view.SetStopButtonState(true);
		} else {
			view.SetStartButtonState(true);
			view.SetStopButtonState(false);
		}

		String text = "";
		switch (state) {
		case RUNNING:
			text = "Daemon is running";
			break;
		case STOPPED:
			text = "Daemon has stopped";
			break;
		case ERROR:
			text = "Daemon failed!";
			break;
		}

		view.SetStatus(text);
	}

	//button to start daemon process
	public void OnStartClick() {
		model.set(KEY_ENABLED, true);
		DaemonThread();
	}

	//button to stop daemon process
	public void OnStopClick() {
		model.set(KEY_ENABLED, false);
		DaemonThread();
	}
	
	/*========================================================================================
     * Daemon thread stuff
     * ======================================================================================*/
	//handles when to turn on and off daemon through installation
	private void DaemonThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (IsDaemonEnabled()) {
					try {
						pauseTimer = true;
						StartDaemon(5000);
					} catch(Exception e){
						System.err.println(">>>>> ERROR: Could not start daemon process.");
					} finally {
						pauseTimer = false;
					}
				} else {
					daemonService.getDaemon().stop();
				}
			}
		}).start();
	}

	private void StartDaemon(long timeout) throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeout * 1000L * 1000L;
		while(System.nanoTime() < endTime && (daemonService.getDaemon().getState() != DaemonContribution.State.RUNNING)) {
			Thread.sleep(100);
		}
	}
	
	/*========================================================================================
     * Main Overrides
     * ======================================================================================*/
	@Override
	public void openView() {

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!pauseTimer) {
							UpdateStatusUI();
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
		if (uiTimer != null) {
			uiTimer.cancel();
		}
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Apply the settings to the daemon on program start in the Installation pre-amble
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"http://127.0.0.1:" + PORT + "/RPC2\")");	
		
//		writer.appendLine(VAR_READ_STRING_VARIABLE+"=\"stringreadvar\"");
//		writer.appendLine("def "+FUN_GET_READ_STRING+"():");
//			writer.appendLine("return "+VAR_READ_STRING_VARIABLE); 
//		writer.appendLine("end");
//		
//		writer.appendLine("def "+FUN_MSG_CHECK+"(s):");
//			writer.appendLine("if(s=="+VAR_READ_STRING_VARIABLE+"):");
//				writer.appendLine("return True");
//			writer.appendLine("else:");
//				writer.appendLine("return False");
//			writer.appendLine("end");
//		writer.appendLine("end");
		
		
	}
}
