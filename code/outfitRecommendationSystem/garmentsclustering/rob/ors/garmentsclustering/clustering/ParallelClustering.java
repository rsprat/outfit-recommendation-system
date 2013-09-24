package rob.ors.garmentsclustering.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import rob.ors.core.config.Paths;
import rob.ors.core.model.api.Category;
import rob.ors.core.polyvore.PolyvoreCategoryTree;

public class ParallelClustering {

	private static class ProcessKiller implements Runnable
	{
		private Process[] processes;
		public ProcessKiller(Process[] processes)
		{
			this.processes = processes;
		}
		public void run()
		{
			LOGGER.info("STARTING TO KILL PROCESSES");
			try{
				for(int i =0;i<processes.length;i++)
				{
					processes[i].destroy();
					LOGGER.info("KILLING P"+i);
				}
				
			}catch(Exception ex)
			{
				LOGGER.error("Unable to kill the childs", ex);
			}
		}
	}
	private static Logger LOGGER = Logger.getLogger(ParallelClustering.class.getCanonicalName());
	public static void main(String[] args)
	{
		
		try
		{		
			List<String> cmd = new LinkedList<String>();
			//cmd.add("C:\\Windows\\System32\\cmd.exe");
			//cmd.add("/c");
		
			BufferedReader br = new BufferedReader(new FileReader(Paths.RUN_CMD));		
			String line ="";
			while((line = br.readLine())!=null){cmd.add(line);}	
			br.close();
			
	
			Object[] cats = PolyvoreCategoryTree.getSubtreeAsSet(0).toArray();
			int[] ids = new int[cats.length];
			int numProcesses = 3;
			int[][] pcats = new int[numProcesses][(int) Math.ceil(((float)ids.length)/numProcesses)];
			for(int i=0;i<cats.length;i++)
			{
				pcats[i%numProcesses][(int) Math.floor(((float)i)/numProcesses)] = ((Category)cats[i]).getId();
			}
			Process[] processes = new Process[numProcesses];
			for(int i = 0;i<numProcesses;i++)
			{
				try {
					List<String> myCMD = new LinkedList<String>(cmd);
					for(int j = 0;j<pcats[i].length;j++)myCMD.add(""+pcats[i][j]);
					//String fullCommand = " /c "+runCMD+" "+Arrays.toString(pcats[i]).replaceAll("[\\[\\],]","");
					LOGGER.info(myCMD.toString());	
					
					System.out.println();
					ProcessBuilder builder=new ProcessBuilder(myCMD.toArray(new String[0]));
					builder.redirectErrorStream();
					builder.redirectOutput(new File(Paths.OUTPUT_FOLDER+"P_"+i+".txt"));
					builder.redirectError(new File(Paths.OUTPUT_FOLDER+"P_"+i+".txt"));
					//builder.inheritIO();
					processes[i] = builder.start();				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread(new ProcessKiller(processes)));
			
			
			for(int i = 0;i<numProcesses;i++)
			{
				
				processes[i].waitFor();
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}
