package rob.ors.core.utils;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class DirectoryReader {

 
	/**
	 * Get a list of the names of the files wit the given extension stored in the path.
	 * @param path
	 * @param extension
	 * @return
	 */
	public static List<String> getFileNames(String path,String extension)
	{
		List<String> files = new LinkedList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile() &&  listOfFiles[i].getName().endsWith("."+extension) )
			{
				files.add(listOfFiles[i].getName());
			}
		} 		
		return files;
    }
	
	
	public static List<String> getFileNames(String path,String extension,int count)
	{
		List<String> files = new LinkedList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Random r = new Random();
		for (int i = 0; (i <listOfFiles.length) && i<count; i++)
		{
			int k= r.nextInt(listOfFiles.length);
			if (listOfFiles[k].isFile() &&  listOfFiles[k].getName().endsWith("."+extension) && !files.contains(listOfFiles[k].getName()))
			{
				files.add(listOfFiles[k].getName());
			}
		} 		
		return files;
    }


}
