package restore;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.ShutdownChannelGroupException;

/** 
 * The DirectoryCopy classes primary function is to receive a source and destination path and copy the source to the destination using apache FileUtils
 * @author Michael Pierre
 *
 */
public class DirectoryCopy 
{
	private String source_ ; 
	private String destination_;
    public void copy()
    {
        //
        // An existing directory to copy.
        //
        File srcDir = new File(source_);

        //
        // The destination directory to copy to. This directory
        // doesn't exists and will be created during the copy
        // directory process.
        //
        File destDir = new File(destination_);

        try 
        {
            //
            // Copy source directory into destination directory
            // including its child directories and files. When
            // the destination directory is not exists it will
            // be created. This copy process also preserve the
            // date information of the file.
            //
        	try
        	{
        		FileUtils.copyDirectory(srcDir, destDir);
        	}
        	catch(FileNotFoundException d)
        	{
        		System.out.println(d.getMessage());
        	}
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void setSource(String source)
    {
    	source_ = source;
    }
    
    public void setDestination(String destination)
    {
    	destination_ = destination;
    }
}