package restore;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;

import gui.GUI;

/**
 * The following class contains the methods needed to complete various program needs. For example the search algorithm and discrepancy check methods are 
 * located here. The class also initializes the GUI. 
 * @author Michael Pierre
 *
 */
public class Restore 
{
	private static String backupPath = "C:/Users/PierreMi/Documents/Store_1_Test/Store1_Backup";
	private static String originalPath = "C:/Users/PierreMi/Documents/Store_1_Test/Store1";
	private static File[] backupFiles;
	private static String[] bDirectories;
	private static String[] oDirectories;
	private static File backupFolder;
	private static File originalFolder; 
	private static File[] originalFiles;
	private static List<File> fileList2 = new ArrayList<File>();
	private  List<File> resultList;
	private List<String> backupDirList; 
    private List<String> dirList;
	private List<String> originalDirList;
	private List<String> dirSearchList;
	private static List<File> backupFileList;
	private static List<File> originalFileList;
	private List<File> guiList = new ArrayList<File>();
	private static GUI gui = new GUI(); 	
	

	//Inner class handles matching files with the glob pattern in the specified directory
	public static class Finder extends SimpleFileVisitor<Path> 
	{
		private final PathMatcher matcher;
		private int numMatches = 0;

		Finder(String pattern)
		{
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}
   
		// Compares the glob pattern against
		// the file or directory name.
		//if match, add to fileList
		void find(Path file) 
		{ 
			Path name = file.getFileName();
			if (name != null && matcher.matches(name))
			{
				numMatches++;
				System.out.println(file);
				fileList2.add(new File(file.toString())); 
			}
		}

		// Prints the total number of
		// matches to standard out.
		void done()
		{
			System.out.println("Matched: " + numMatches);
			if(numMatches == 0)
			{
				gui.show("No results found");
			}
				
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
		{
			find(file);
			return CONTINUE;
		}

		// Invoke the pattern matching
		// method on each directory.
		@Override
		public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes attrs) 
		{
			find(dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) 
		{
			System.err.println(exc);
			return CONTINUE;
		}
}

	
	static void usage() 
	{
		System.err.println("java Find <path>" + " -name \"<glob_pattern>\"");
		System.exit(-1);
	}

	
	// Searches for a file and creates a resultSet list of the occurrences of that file
	public void searchFile(String fileName)
	{	
		// Trims whitespace from the fileName String
		fileName = fileName.trim();
		System.out.println("Starting search...");
		// Removes all fileList2 objects so search doesn't overlap
		fileList2.removeAll(fileList2);
		String[] fileSplit = null ;
		String pattern = null ;
		
		if(dirList != null)
		{
			if(dirList.size() == 1)
			{
				// If String contains period like sample.txt and new pattern is required which would be pattern = fileName
				if(fileName.contains("."))
				{
					System.out.println("NO PERIOD");
				    fileSplit = fileName.split("\\."); 
					System.out.println("Split " + fileSplit[0] + " " + fileSplit[1]);
					pattern = fileName; 
					System.out.println("Pattern  " + pattern);

				}
				else
					pattern = fileName+"*.*";
				
				// The first directory in the dirList also known as the list of directories within the date range
				Path startingDir = Paths.get(dirList.get(0));
				// New Finder object which essentially searches the startingDirectory for the corresponding glob pattern
				Finder finder = new Finder(pattern);
				
				try 
				{
					Files.walkFileTree(startingDir, finder);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}        
				finder.done();
				// Sort fileList by newest last modified to oldest last modified
				Collections.sort(fileList2, new Comparator<File>()
				{
				    public int compare(File f1, File f2)
				    {
				        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				    } 
			    });
		       
				gui.setFileList(fileList2);
		        gui.btnGo.setEnabled(true);
			}
			// If dirList is greater than 1 we search a list of directories that are within the date range or if customer directory option is selected 
			// we search a list of the individually selected directories.
			// Does the same operation as above however it is more optimized for a list of directories rather than just one
			else if(dirList.size() > 1)
			{
				System.out.println("Searching multiple directories");
				for(int i = 0;i < dirList.size();i++)
				{					
					if(fileName.contains("."))
					{
						System.out.println("NO PERIOD");
					    fileSplit = fileName.split("\\."); 
						System.out.println("Split " + fileSplit[0] + " " + fileSplit[fileSplit.length - 1]);
				
						if(!fileSplit[fileSplit.length-1].matches(".*\\d+.*"))
							pattern = fileName; 
						
						else
							pattern = fileName+"*.*";

						System.out.println("filename " + fileName);
					}
					else
						pattern = fileName+"*.*";
					
					if(fileName.equals("ALLFILES"))
					{
						pattern = "*";
					}
					Path startingDir = Paths.get(dirList.get(i));
					Finder finder = new Finder(pattern);
				
					try 
					{
						Files.walkFileTree(startingDir, finder);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				Collections.sort(fileList2, new Comparator<File>()
				{
				    public int compare(File f1, File f2)
				    {
				        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				    } 
				});

				gui.setFileList(fileList2);
		        gui.btnGo.setEnabled(true);
			}
		}
		else if(dirList == null)
		{
			gui.show("ERROR: No directory ");
		}
	}
		
	// Main method initializes and shows GUI
	public static void main(String [] args)
	{	
		System.out.println("Start");
		gui.initialize();
		gui.frmStoreSync.setVisible(true);
	}
	
	public void setGuiFileList(List<File> fl)
	{
		fl = guiList;
	}
	// The following method sets the dirList List which is a list of directories within the date range
	public void setDirList(List<String> dl)
	{	
		dirList = new ArrayList<String>(dl);
		System.out.println("Dir list size " + dirList.size());
	}
	
	public void setBackupDirList(List<String> bl)
	{
		backupDirList = new ArrayList<String>(bl);
	}
	
	public void setOriginalDirList(List<String> ol)
	{
		originalDirList = new ArrayList<String>(ol);
	}
	
	public void setDirSearchList(List<String>dl)
	{
		dirSearchList = new ArrayList<String>(dl);
	}
	
	/**
	 * The following method finds and searches a folder name in between a date range.
	 * The method is used for the search by folder method in the GUI class.
	 * @param folderName
	 */
	public void searchFolder(String folderName)
	{
		// Trims whitespace from folder
		folderName= folderName.trim();
		System.out.println("Working...");
		gui.show("Loading.....");
	    List<Path> subfolder = null;
	    List<File> tmpList = new ArrayList<File>();
		
	    // dirList contains all the directories within the date range
	    // This for loop gets those directories and all their subdirectories up to 6 levels.
	    // the loop then starts another for loop in which the subfolders are added to a temporary list to be later searched for the matching string
	    // 
	    for(int y = 0;y < dirList.size();y++)
		{
			Path folderPath = FileSystems.getDefault().getPath(dirList.get(y));
			try 
			{
				subfolder = Files.walk(folderPath, 6)
							.filter(Files::isDirectory)
							.collect(Collectors.toList());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			// The following for loop adds the subfolders to a temporary list 
			for(int x = 0;x<subfolder.size();x++)
				tmpList.add(new File(subfolder.get(x).toString()));
		}
	    // The "results" file list adds contains the matching folder names
		List<File> results = new ArrayList<File>();
		// The following for loop searches the temporary list of directory paths for the matching string and adds it to the results list
		for(int i = 0;i < tmpList.size();i++)
	    {
			  if(tmpList.get(i).getName().toLowerCase().matches(folderName.toLowerCase() + ".*"))
			  {
				  results.add(tmpList.get(i));
			  }
			  System.out.println("Folder " + tmpList.get(i).getName());		  
		}
		
		for(int ii = 0;ii < results.size();ii++)
		{
			System.out.println("RESULTS:   " + results.get(ii).getName());
		}
		// sorts the results list from newest to oldest last modified NOTE: Often times the last modified is the backup date
		Collections.sort(results, new Comparator<File>()
		{
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
		    } 
		});
		// Sets the fileList in the GUI class with the results. From there the results will be displayed in the JList
		gui.setFileList(results);
        gui.btnGo.setEnabled(true);  
	}
	
	
}
