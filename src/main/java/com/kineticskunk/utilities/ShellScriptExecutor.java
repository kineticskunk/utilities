package com.kineticskunk.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ShellScriptExecutor {

	public void executeProcess(String location, String shellScript, String[] args) throws IOException,  InterruptedException {  

		final File executorDirectory = new File(location);   

		ProcessBuilder processBuilder = new ProcessBuilder(shellScript, args.toString());  
		processBuilder.directory(executorDirectory);  
		Process process = processBuilder.start();  

		try {  
			int shellExitStatus = process.waitFor();  
			if (shellExitStatus != 0) {  
				//logger.info("Successfully executed the shell script");  
			}  
		} catch (InterruptedException ex) {  
			//logger.error("Shell Script preocess is interrupted");  
		}  

	}

	static class StreamGobbler extends Thread {
		InputStream is;

		String type;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					System.out.println(type + ">" + line);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("USAGE: java ShellScriptExecutor script");
			System.exit(1);
		}

		try {
			String osName = System.getProperty("os.name");

			switch (osName.toLowerCase()) {
			case "":
				
			}

			String[] cmd = new String[2];
			cmd[0] = "/bin/sh"; // should exist on all POSIX systems
			cmd[1] = args[0];

			Runtime rt = Runtime.getRuntime();
			System.out.println("Execing " + cmd[0] + " " + cmd[1] );
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}



	/**
	 * This class executes the shell script on the remote server
	 * Requires the jSch java library
	 * @author Saket kumar
	 *
	 */

	public class ShellExecuter{

		private static final String USERNAME ="root"; // username for remote host
		private static final String PASSWORD ="password"; // password of the remote host
		private String host = "113.33.111.111"; // remote host address
		private static final int port=22;

		/**
		 * This method will execute the script file on the server.
		 * This takes file name to be executed as an argument
		 * The result will be returned in the form of the list
		 * @param scriptFileName
		 * @return
		 */
		public List<String> executeFile(String scriptFileName)
		{
			List<String> result = new ArrayList<String>();
			try
			{

				/**
				 * Create a new Jsch object
				 * This object will execute shell commands or scripts on server
				 */
				JSch jsch = new JSch();

				/*
				 * Open a new session, with your username, host and port
				 * Set the password and call connect.
				 * session.connect() opens a new connection to remote SSH server.
				 * Once the connection is established, you can initiate a new channel.
				 * this channel is needed to connect to remotely execution program
				 */
				Session session = jsch.getSession(USERNAME, host, port);
				session.setConfig("StrictHostKeyChecking", "no");
				session.setPassword(PASSWORD);
				session.connect();

				//create the excution channel over the session
				ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

				// Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
				InputStream in = channelExec.getInputStream();

				// Set the command that you want to execute
				// In our case its the remote shell script
				channelExec.setCommand("sh "+scriptFileName);

				// Execute the command
				channelExec.connect();

				// Read the output from the input stream we set above
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;

				//Read each line from the buffered reader and add it to result list
				// You can also simple print the result here 
				while ((line = reader.readLine()) != null)
				{
					result.add(line);
				}

				//retrieve the exit status of the remote command corresponding to this channel
				int exitStatus = channelExec.getExitStatus();
				
				//Safely disconnect channel and disconnect session. If not done then it may cause resource leak
				channelExec.disconnect();
				session.disconnect();

				if(exitStatus < 0){
					// System.out.println("Done, but exit status not set!");
				}
				else if(exitStatus > 0){
					// System.out.println("Done, but with error!");
				}
				else{
					// System.out.println("Done!");
				}

			}
			catch(Exception e)
			{
				System.err.println("Error: " + e);
			}
			return result;
		}

	}
}