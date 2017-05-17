package com.kineticskunk.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandLineInteractor {

	private static Logger logger;
	private ShellScriptExecutor sse;
	
	private String strShellScriptLocation;
	private String strCommand;
	private String strResponse;
	public boolean blnWaitForResponce;

	public CommandLineInteractor () {
		logger = LogManager.getLogger(CommandLineInteractor.class.getName());
		sse = new ShellScriptExecutor();
		
		strShellScriptLocation = null;
		strCommand = null;
		strResponse = null;
		blnWaitForResponce = false;
	}

	public void tearDown() {
		strShellScriptLocation = null;
		strCommand = null;
		strResponse = null;
		blnWaitForResponce = false;
	}

	public void setShellScriptLocation(String strShellScriptLocation) {
		
		if (!strShellScriptLocation.endsWith(System.getProperty("file.separator"))) {
			this.strShellScriptLocation = strShellScriptLocation + System.getProperty("file.separator");
		} else {
			this.strShellScriptLocation = strShellScriptLocation;
		}
	}

	public String getShellScriptLocation() {
		return strShellScriptLocation;
	}

	public void setCommand(String strCommand) {
		this.strCommand = strCommand;
	}

	public String getCommand() {
		return strCommand;
	}

	public void setResponse(String strResponse) {
		this.strResponse = strResponse; 
	}

	public String getResponse() {
		return strResponse;
	}

	public void setWaitForResponse(boolean blnWaitForResponce) {
		this.blnWaitForResponce = blnWaitForResponce; 
	}

	public boolean getWaitForResponse() {
		return blnWaitForResponce;
	}

	public void executeProcessBuilderCommand() {
		ProcessBuilder pb = new ProcessBuilder("bash" , "-c", "cd / ; cd " + this.getShellScriptLocation() + " ; " + this.getCommand());
		pb.redirectErrorStream(true);
		try {
			Process shell = pb.start();
			if (this.getWaitForResponse() == true) {
				InputStream shellIn = shell.getInputStream();
				shell.waitFor();
				this.setResponse(this.convertInputStreamToStr(shellIn));
				shellIn.close();
			}
		} catch (IOException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		}
	}
	
	public InputStream getResourceInputStream(String configFileName) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFileName);
		return is;
	}
	
	public String runShellScript(String script, String[] args, boolean wait) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("bash" , "-c", "sh " + script + args);
		pb.redirectErrorStream(true);
		try {
			Process shell = pb.start();
			if (wait == true) {
				InputStream shellIn = shell.getInputStream();
				shell.waitFor();
				String responce = this.convertInputStreamToStr(shellIn);
				shellIn.close();
				return responce;
			} else {
				return "NOT_WAITING_FOR_A_RESPONCE";
			}
		} catch (IOException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		}
		return null;
	}
	
	public String runBashScript(String script, String[] args, boolean wait) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("bash" , "-c", "sh " + this.newConvertInputStreamToStr(this.getResourceInputStream(script)) + args);
		pb.redirectErrorStream(true);
		try {
			Process shell = pb.start();
			if (wait == true) {
				InputStream shellIn = shell.getInputStream();
				shell.waitFor();
				String responce = this.convertInputStreamToStr(shellIn);
				shellIn.close();
				return responce;
			} else {
				return "NOT_WAITING_FOR_A_RESPONCE";
			}
		} catch (IOException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		}
		return null;
	}

	public String convertInputStreamToStr(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					System.out.println(n);
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString().trim();
		} else {
			return "";
		}
	}
	
	public String testFIS(InputStream fis) throws IOException {
		Scanner scanner =  new Scanner(fis,"UTF-8");
		String inputStreamString = scanner.useDelimiter("\\A").next();
		scanner.close();
        return inputStreamString;
    }



	public String newConvertInputStreamToStr(InputStream is) throws IOException {
		StringBuffer output = new StringBuffer();
		if (is != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = "";			
				while ((line = reader.readLine())!= null) {
					if (line.endsWith("\\n")) {
						line.replaceAll("\\n", " ");
					}
					System.out.println(line);
					output.append(line + "\n");
				}
			} finally {
				is.close();
			}
			return output.toString().trim();
		} else {
			return "";
		}
	}

	public String executeRuntimeCommand() {

		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(new String[]{"bash", "-c", this.getCommand()});
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}

	public String executeRuntimeCommand(String strCommand) throws IOException {
		Process p = Runtime.getRuntime().exec(new String[]{"bash" , "-c", strCommand});
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line="";
		String allLine="";
		int i=1;
		while((line=r.readLine()) != null){
			System.out.println(i+". "+line);
			allLine=allLine+""+line+"\n";
			if(line.contains("Console LogLevel: debug"))
				break;
			i++;
		}
		return allLine;
	}

	public String executeRuntimeCommand(String strCommand, boolean blnWaitForResponse) {

		ProcessBuilder pb = new ProcessBuilder("bash" , "-c", strCommand);
		pb.redirectErrorStream(true);
		try {
			Process shell = pb.start();
			if (blnWaitForResponse == true) {
				InputStream shellIn = shell.getInputStream();
				int shellExitStatus = shell.waitFor();
				String strOutput = this.convertInputStreamToStr(shellIn).replace("\n", " ");
				logger.info("Command: " + (char)34 + strCommand + " exited with a status of " + (char)34 + shellExitStatus + (char)34 + " and a responce of " + strOutput);
				shellIn.close();
				return strOutput;
			} else {
				return "not_waiting_for_response";
			}
		}

		catch (IOException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		}

		catch (InterruptedException e) {
			logger.error("Error occured while executing command "  + (char)34 + this.getCommand() + (char)34 + " error description: " + e.getMessage());
		}
		return null;
	}

	public String runCommandline(String args[]) throws IOException {
		Process process = new ProcessBuilder(args).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuffer output = new StringBuffer();
		while ((line = br.readLine())!= null) {
			output.append(line);
		}
		return output.toString();
	}
}