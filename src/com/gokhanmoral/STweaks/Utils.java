package com.gokhanmoral.STweaks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.util.Log;

public class Utils {
	private static String LOG_TAG = Utils.class.getName();
	private static String scriptSeparator = "[%nexttweaks%]";

	private static Process rootProcess = null;
	private static BufferedWriter consoleIn = null;
	private static BufferedReader consoleOut = null;
	private static BufferedReader consoleError = null;

	private static void initializeUtils() {
		try {
			rootProcess = Runtime.getRuntime().exec("/system/xbin/su"); // get
																		// the
																		// root
																		// access
			consoleIn = new BufferedWriter(new OutputStreamWriter(
					rootProcess.getOutputStream()));
			consoleOut = new BufferedReader(new InputStreamReader(
					rootProcess.getInputStream()));
			consoleError = new BufferedReader(new InputStreamReader(
					rootProcess.getErrorStream()));
		} catch (IOException e) {
			rootProcess = null;
			consoleIn = null;
			consoleOut = null;
			consoleError = null;
			e.printStackTrace();
		}
	}

	private static Boolean isUtilsInitilized() {
		if (rootProcess == null || consoleIn == null || consoleOut == null
				|| consoleError == null) {
			return false;
		}
		return true;
	}

	public static void reset() {
		if (rootProcess != null) {
			rootProcess.destroy();
		}
		rootProcess = null;
		consoleIn = null;
		consoleOut = null;
		consoleError = null;
	}

	public Utils() {
		initializeUtils();
	}

	public static boolean canRunRootCommandsInThread() {
		boolean isOk = false;
		String line = "";
		final String echoStr = "suPermsOk";

		Log.d(LOG_TAG, "=== canRunRootCommands ===");

		if (isUtilsInitilized() == false) {
			initializeUtils();
			if (isUtilsInitilized() == false) {
				return false;
			}
		}

		try {
			consoleIn.write("echo " + echoStr + "\n");
			consoleIn.flush();

			boolean finished = false;
			long startTime = 0;
			long timeout = 30000;
			while (!finished) {
				if (consoleOut.ready()) {
					line = consoleOut.readLine();
					if (line != null) {
						Log.d(LOG_TAG, "--> Line received: " + line);
						if (line.equalsIgnoreCase(echoStr)) {
							isOk = true;
						}
					}

					finished = true;
				} else if (consoleError.ready()) {
					line = consoleError.readLine() + "\r\n";
					Log.d(LOG_TAG, "--> Error received: " + line);
					if (line != null)
						finished = true;
				} else {
					if (startTime == 0) {
						startTime = System.currentTimeMillis();

						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
						}
					} else if (System.currentTimeMillis() > startTime + timeout) {
						finished = true;
						Log.d(LOG_TAG, "No root access, timeout!");
					}
				}
			}

		} catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream after su failed, meaning that the device is not rooted
			Log.d(LOG_TAG, "Exception: No root access! " + e.getMessage());
		}

		return isOk;
	}

	public static String executeRootCommandInThread3(String command) {
		Log.i(LOG_TAG, "[Running command:" + command + "]");

		String consoleOutStr = "";
		String line = "";
		String lineErr = "";

		if (isUtilsInitilized() == false) {
			initializeUtils();
			if (isUtilsInitilized() == false) {
				return consoleOutStr;
			}
		}

		try {
			if (null != command && command.length() > 0) {
				consoleIn.write(command + "\n" + "echo " + scriptSeparator
						+ "\n"); // throws IOException
				consoleIn.flush(); // throws IOException

				// Parse standard error and standard out
				boolean finished = false;
				while (!finished) {
					if (consoleOut.ready()) {
						line = consoleOut.readLine();
						if (line == null) {
							finished = true;
						} else {
							Log.d(LOG_TAG, "--> Line received: " + line);
							if (line.equals(scriptSeparator)) {
								finished = true;
								while (consoleError.ready()) {
									lineErr = consoleError.readLine();
									if ((lineErr != null) && !lineErr.isEmpty())
										Log.d(LOG_TAG, "--> Error received: "
												+ lineErr);
								}
							} else {
								consoleOutStr += line + "\r\n";
							}
						}
					} else if (consoleError.ready()) {
						lineErr = consoleError.readLine();
						if ((lineErr != null) && !lineErr.isEmpty())
							Log.d(LOG_TAG, "--> Error received: " + lineErr);
					} else {
						// Log.d(LOG_TAG,
						// "--> not ready for stdout or stderr!");
						/*
						 * try { Thread.sleep(1); } catch (InterruptedException
						 * e) { //e.printStackTrace(); }
						 */

						// TODO: Add a "timeout" code!
					}
					/*
					 * try { Thread.sleep(1); } catch (InterruptedException e) {
					 * //e.printStackTrace(); }
					 */
				}// while
			}// if
		} catch (IOException ex) {
			Log.w(LOG_TAG + "executeRootCommandInThread",
					"Running command failed: " + command, ex);
		}

		Log.i(LOG_TAG, "[Finished command:" + command + "]");
		return consoleOutStr;
	}
}