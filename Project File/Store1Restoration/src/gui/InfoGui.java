package gui;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;

import restore.DirectoryCopy;
import restore.FindImage;
import restore.FindImage2;
import restore.Restore.Finder;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * The InfoGui class handles operation for specific file/folder look up the user can restore a folder and all it subfolders or specific files contained in
 * a folder throughout its backup history 
 * @author Michael Pierre
 *
 */
public class InfoGui 
{

	private JFrame frmFileHistory;
	private String fileName;
	private DefaultListModel<String>historyModel;	
	private DefaultListModel<String>inclModel;		
	private List<File> fileList_;
	private JTextArea textArea;
	private JLabel fileIcon; 
	JList<String> historyList;
	private JButton btnRestore;
	private int size_;
	JList<String> incList;
	JButton btnRestore_1;
	static List<File>fldFileList; 
	private boolean isDirectory; 
	String restoreP ;
	JFileChooser chooser1 ;
	private JLabel lblBackups;
	JLabel lblFlifcon;
	JFileChooser chooser2 ;
	private JLabel lblFiles;
	private JLabel lblDescription;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					InfoGui window = new InfoGui(true);
					window.frmFileHistory.setVisible(true);	
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public void getHistory(List<File> fileList)
	{
		fileList_ = new ArrayList<File>(fileList);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		for(int i = 0;i< fileList.size();i++)
		{
			historyModel.addElement(i+". "+ fileList.get(i).getName() + " Last Modified, " +  sdf.format(fileList.get(i).lastModified()));
		}
		historyList.setSelectedIndex(0); 
		historyList.ensureIndexIsVisible(0);
	}
	
	/**
	 * Create the application.
	 */
	public InfoGui(boolean isDirectory1)
	{
		isDirectory = isDirectory1;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		inclModel = new  DefaultListModel<String>();
		historyModel = new DefaultListModel<String>();
		frmFileHistory = new JFrame();
		frmFileHistory.setTitle("History");
		frmFileHistory.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmFileHistory.setBounds(100, 100, 1060, 490);
		frmFileHistory.getContentPane().setLayout(null);
		fileIcon = new JLabel("");
		fileIcon.setBounds(35, 11, 200, 162);
		frmFileHistory.getContentPane().add(fileIcon);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 228, 604, 213);
		frmFileHistory.getContentPane().add(scrollPane);
		historyList = new JList<String>(historyModel);
		scrollPane.setViewportView(historyList);
		historyList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{	
				lblFlifcon.setIcon(new ImageIcon(InfoGui.class.getResource("/fileImage/rsz_question.png")));
				//incList.setSelectedIndex(0); 
			    //incList.ensureIndexIsVisible(0);
				//fileIcon.setIcon(new ImageIcon(InfoGui.class.getResource("/restore/rsz_1microsoft_excel_2013_logosvg.png")));
				textArea.setCaretPosition(0);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Object selected = historyList.getSelectedValue();
				int index = historyList.getSelectedIndex();
			    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
			    FindImage fi = new FindImage();

			    if(fileList_.get(index).isFile())
			    {
			    	String [] getFileType = fileList_.get(index).getName().split("\\.");
			    	String fileType = getFileType[getFileType.length-1];
			    	System.out.println("TYPE   " + fileType);
			    	fi.setFileType(fileType); 
			    }
			    else
			    	fi.setFileType("");
			    
			    fileIcon.setIcon(new ImageIcon(InfoGui.class.getResource(fi.getIconStr())));
				String fmtDateStr = fileList_.get(index).getAbsolutePath();
				String dateStr = null;
				Date backupDate = null;
				Pattern pattern = Pattern.compile("\\d{1,4}-\\d{1,2}-\\d{1,2}_\\d{1,2}_\\d{1,2}_\\d{1,2}");
				Matcher matcher = pattern.matcher(fmtDateStr);
				if (matcher.find())
				{
				    System.out.println("Match " + matcher.group(0));
				    dateStr = matcher.group(0);
				    try 
				    {
						backupDate = sdf2.parse(dateStr);
					}
				    catch (ParseException e) 
				    {
						e.printStackTrace();
					}
				}
				System.out.println("Backup date   " + backupDate);
				
				fldFileList = new ArrayList<File>();
				inclModel.clear();
				String p = fileList_.get(index).getAbsolutePath();
				Path startingDir = Paths.get(p);

				Finder finder = new Finder("*.*");
				
				try 
				{
					Files.walkFileTree(startingDir, finder);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}        
				finder.done();
				
				
				for(int i = 0;i < fldFileList.size();i++)
				{
					inclModel.addElement(i+". " + fldFileList.get(i).getName() + "  Last Modified, " + sdf.format(fldFileList.get(i).lastModified()));
				}
				
					// FindImage2 fi2 = new FindImage2();

				    
				   	//String [] getFileType = fldFileList.get(index).getName().split("\\.");
				    //	String fileType = getFileType[getFileType.length-1];
				    //	System.out.println("TYPE   " + fileType);
				    //	fi2.setFileType(fileType); 
					//	lblFlifcon.setIcon(new ImageIcon(InfoGui.class.getResource(fi2.getIconStr())));

				
				System.out.println(fileList_.get(index).getName());
				textArea.setText("");
				textArea.setText("File Name: " + fileList_.get(index).getName() + "\n" + "Last Modified: " 
				+ sdf.format(fileList_.get(index).lastModified()) + "\n" + "File Path: " + fileList_.get(index).getAbsolutePath() + 
				  "\n" + "Backup Date: " + backupDate);
				textArea.setCaretPosition(0);
			}
		});
		
		historyList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		historyList.addMouseListener(new MouseAdapter() 
		{
		    public void mouseClicked(MouseEvent evt)
		    {
		    	  if(evt.getClickCount() == 2)
		    	  {
		    		  if(!isDirectory)
		    		  {
		    		     int index = historyList.locationToIndex(evt.getPoint());
		    		     ListModel dlm = historyList.getModel();
		    		     Object item = dlm.getElementAt(index);
		    		     System.out.println("INDEX       " + index);
		    		     System.out.println("Selected " + item.toString());
		    		     historyList.ensureIndexIsVisible(index);
		    		     System.out.println("Double clicked on " + item.toString() + " FILE LIST LENGTH " + fileList_.size());

		    		     try 
		    			 {
		    				 System.out.println("File Path " + fileList_.get(index).getAbsolutePath());
		    				// System.out.println("PARENT " + file.getParentFile());
		    				 Desktop.getDesktop().open(fileList_.get(index).getParentFile());
		    			 }   	    
		    			 catch (IOException e) 
		    			 {
		    				 e.printStackTrace();
		    			 }
		    		  }
		    		  else
		    		  {
			    		     int index = historyList.locationToIndex(evt.getPoint());
			    		     try 
			    			 {
			    				 Desktop.getDesktop().open(fileList_.get(index).getParentFile());
			    			 }   	    
			    			 catch (IOException e) 
			    			 {
			    				 e.printStackTrace();
			    			 }		    		  
		    		  }
		    	 }
		    }
		});
		//}
		
		lblBackups = new JLabel("Backups");
		lblBackups.setBounds(10, 203, 64, 14);
		frmFileHistory.getContentPane().add(lblBackups);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(275, 25, 335, 176);
		frmFileHistory.getContentPane().add(scrollPane_1);
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(642, 25, 392, 264);
		frmFileHistory.getContentPane().add(scrollPane_2);
	
		incList = new JList<String>(inclModel);
		scrollPane_2.setViewportView(incList);
		incList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{
				FindImage2 fi2 = new FindImage2();
				int index = incList.getSelectedIndex();
				if(index >= 0)
				{
					try
					{
						System.out.println("fld SIZE    " + fldFileList.size() + "   " + fldFileList.get(index).getName());
						String [] getFileType = fldFileList.get(index).getName().split("\\.");
						System.out.println("Selected Index " + index);
						String fileType = getFileType[getFileType.length-1];
						System.out.println("TYPE   " + fileType);
						fi2.setFileType(fileType); 
						lblFlifcon.setIcon(new ImageIcon(InfoGui.class.getResource(fi2.getIconStr())));
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			}
		});
		incList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		incList.addMouseListener(new MouseAdapter() 
		{
		    public void mouseClicked(MouseEvent evt)
		    {
		    	  if(evt.getClickCount() == 2)
		    	  {
		    		     int index = incList.locationToIndex(evt.getPoint());
		    		     ListModel dlm = incList.getModel();
		    		     Object item = dlm.getElementAt(index);
		    		     System.out.println("INDEX       " + index);
		    		     System.out.println("Selected " + item.toString());
		    		     incList.ensureIndexIsVisible(index);
		    		     System.out.println("Double clicked on " + item.toString() + " FILE LIST LENGTH " + fileList_.size());

		    		     try 
		    			 {
		    				 System.out.println("File Path " + fldFileList.get(index).getAbsolutePath());
		    				// System.out.println("PARENT " + file.getParentFile());
		    				 Desktop.getDesktop().open(fldFileList.get(index).getParentFile());
		    			 }   	    
		    			 catch (IOException e) 
		    			 {
		    				 e.printStackTrace();
		    			 }
		    	 }
		    }
		});
		
		lblFlifcon = new JLabel("flIfcon");
		lblFlifcon.setBounds(642, 300, 80, 80);
		frmFileHistory.getContentPane().add(lblFlifcon);
		
		btnRestore_1 = new JButton("Restore");
		btnRestore_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				int index = incList.getSelectedIndex();
				if(!textArea.getText().equals("") && !historyList.isSelectionEmpty() && !incList.isSelectionEmpty())
				{
					btnRestore_1.setEnabled(false);
					textArea.setCaretPosition(textArea.getDocument().getLength());

					chooser2 = new JFileChooser(getMyDocuments())
					{
						@Override
						public void approveSelection()
						{
							File check = new File(chooser2.getSelectedFile().getAbsolutePath() + "\\" + fileList_.get(index).getName());

							if(check.exists() && getDialogType() == SAVE_DIALOG)
							{
								int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
								switch(result)
								{
									case JOptionPane.YES_OPTION:
										super.approveSelection();
										return;
									case JOptionPane.NO_OPTION:
										return;
									case JOptionPane.CLOSED_OPTION:
										return;
									case JOptionPane.CANCEL_OPTION:
										cancelSelection();
										return;
								}
							}
							super.approveSelection();
						} 
				};
				chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser2.setMultiSelectionEnabled(false);
			    List<String> dirList = new ArrayList<String>();
				int option = chooser2.showSaveDialog(frmFileHistory);
				boolean work = false;
				if (option == JFileChooser.APPROVE_OPTION)
				{	
					restoreP = chooser2.getSelectedFile().getAbsolutePath().replace("\\", "/");;
					restoreP = restoreP + "/";
					System.out.println("Restore point " + restoreP);
					work=true;
				}
				if(work)
				{	
						show("\n"+"Restoring....");
						Object selected = incList.getSelectedValue();
					
						if(selected != null)
						{ 		    		    
		    			        File dest = new File(restoreP + fldFileList.get(index).getName());
		    		     
		    			        if(!dest.exists())
		    			        {
		    			        	try 
		    			        	{
		    			        		FileUtils.copyFile(fldFileList.get(index).getAbsoluteFile(), dest);
		    			        	} 
		    			        	catch (IOException e1) 
		    			        	{
		    			        		e1.printStackTrace();
		    			        	}
						
		    			        	try 
		    			        	{
		    			        		String s = dest.getParentFile().toString();
		    			        		Desktop.getDesktop().open(new File(s));
		    							btnRestore_1.setEnabled(true);

		    			        	}   	    
		    			        	catch (IOException e1) 
		    			        	{
		    			        		e1.printStackTrace();
		    			        	}
		    			        }
		    			      
						}
						
						btnRestore_1.setEnabled(true);
						show("Complete");
						try 
						{
		    				 Desktop.getDesktop().open(new File(restoreP));
		 				   	 btnRestore.setEnabled(true);

		    			}   	    
		    			catch (IOException e1) 
		    			{
		    				 e1.printStackTrace();
		    			}
						btnRestore_1.setEnabled(true);
				}
				else				
					btnRestore_1.setEnabled(true);
				
				}
			}
		});
		btnRestore_1.setIcon(new ImageIcon(InfoGui.class.getResource("/fileImage/rsz_restore.png")));
		btnRestore_1.setMargin(new Insets(2, 0, 2, 0));
		btnRestore_1.setBounds(945, 300, 89, 23);
		frmFileHistory.getContentPane().add(btnRestore_1);
		
		btnRestore = new JButton("Restore");
		// Action listener for restore somewhat acts as a method and performs the restoring of the user selected file
		btnRestore.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int index = historyList.getSelectedIndex();

				if(!textArea.getText().equals(""))
				{
					btnRestore.setEnabled(false);
					textArea.setCaretPosition(textArea.getDocument().getLength());
					// Creates new JFileChooser which allows the user to select their desired restore location. The paramenter getMyDocuments() is the path 
					// of the users my documents folder 
				    chooser1 = new JFileChooser(getMyDocuments())
					{
						@Override
						public void approveSelection()
						{
							File check = new File(chooser1.getSelectedFile().getAbsolutePath() + "\\" + fileList_.get(index).getName());

							if(check.exists() && getDialogType() == SAVE_DIALOG)
							{
								int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
								switch(result)
								{
									case JOptionPane.YES_OPTION:
										super.approveSelection();
										return;
									case JOptionPane.NO_OPTION:
										return;
									case JOptionPane.CLOSED_OPTION:
										return;
									case JOptionPane.CANCEL_OPTION:
										cancelSelection();
										return;
								}
							}
							super.approveSelection();
						} 
				};
				chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser1.setMultiSelectionEnabled(false);
			    List<String> dirList = new ArrayList<String>();
				int option = chooser1.showSaveDialog(frmFileHistory);
				boolean work = false;
				if (option == JFileChooser.APPROVE_OPTION)
				{	
					restoreP = chooser1.getSelectedFile().getAbsolutePath().replace("\\", "/");;
					restoreP = restoreP + "/";
					System.out.println("Restore point " + restoreP);
					// If option approved work flag is set to true which begins the restore
					work=true;
				}
				
				if(work)
				{	
					show("\n"+"Restoring....");
					Object selected = historyList.getSelectedValue();
					if(!isDirectory)
					{
						if(selected != null)
						{ 		    		    
		    			        File dest = new File(restoreP + fileList_.get(index).getName());
		    		     
		    			        if(!dest.exists())
		    			        {
		    			        	try 
		    			        	{
		    			        		FileUtils.copyFile(fileList_.get(index).getAbsoluteFile(), dest);
		    			        	} 
		    			        	catch (IOException e1) 
		    			        	{
		    			        		e1.printStackTrace();
		    			        	}
						
		    			        	try 
		    			        	{
		    			        		String s = dest.getParentFile().toString();
		    			        		System.out.println("OPEN DIR  " + s);
		    			        		Desktop.getDesktop().open(new File(s));
		    							btnRestore.setEnabled(true);
		    			        	}   	    
		    			        	catch (IOException e1) 
		    			        	{
		    			        		e1.printStackTrace();
		    			        	}
		    			        }
		    			      
						}
					}
					else if(isDirectory)
					{
						// Creates DirectoryCopy object which performs the copy of the selected store1 directory to the restore location
						 DirectoryCopy dc = new DirectoryCopy(); 
						 File dest = new File(restoreP + fileList_.get(index).getName());
		    		     dc.setSource(fileList_.get(index).getAbsolutePath());
		    		     dc.setDestination(dest.getAbsolutePath());
		    		     dc.copy();
		    		     
		    		     try 
		    			 {
		    				 String s = dest.getParentFile().toString();
		    				 Desktop.getDesktop().open(new File(s));
		 				   	 btnRestore.setEnabled(true);

		    			 }   	    
		    			 catch (IOException e1) 
		    			 {
		    				 e1.printStackTrace();
		    			 }
		    		     btnRestore.setEnabled(true);

					}
					
					show("Completed");
				    try 
	    			{
	    				 Desktop.getDesktop().open(new File(restoreP));
	 				   	 btnRestore.setEnabled(true);

	    			}   	    
	    			catch (IOException e1) 
	    			{
	    				 e1.printStackTrace();
	    			}
					btnRestore.setEnabled(true);

				}
				else				
					btnRestore.setEnabled(true);
				
				}
			}
		});
		btnRestore.setIcon(new ImageIcon(InfoGui.class.getResource("/fileImage/rsz_restore.png")));
		btnRestore.setBounds(84, 199, 89, 23);
		btnRestore.setMargin(new Insets(2, 0, 2, 0));

		lblDescription = new JLabel("Description");
		lblDescription.setBounds(277, 11, 80, 14);
		frmFileHistory.getContentPane().add(lblDescription);
		lblFiles = new JLabel("Files");
		lblFiles.setBounds(642, 11, 46, 14);
		frmFileHistory.getContentPane().add(lblFiles);
		frmFileHistory.getContentPane().add(btnRestore);
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(InfoGui.class.getResource("/fileImage/background.jpg")));
		label.setBounds(0, 0, 1044, 452);
		frmFileHistory.getContentPane().add(label);
		frmFileHistory.setVisible(true);
	}
	public String getMyDocuments()
	{
		 JFileChooser fr = new JFileChooser();
	     FileSystemView fw = fr.getFileSystemView();
	     File dir = new File(fw.getDefaultDirectory().toString() + "\\" + "RestoredFiles_Store1");
	     boolean successful = dir.mkdir();
			
		 if (successful)
		 {
			 // creating the directory succeeded
			 System.out.println("directory was created successfully");
		 }
			
		 else
		 {
	  		 // creating the directory failed
	  		 System.out.println("failed trying to create the directory");
		 }	
		 restoreP = dir.getAbsolutePath();
	     System.out.println(fw.getDefaultDirectory().toString());
	     return dir.getAbsolutePath();
	}
	public void show(String text)
	{
		textArea.append("\n"+text);
	}
	public void isDirectory(boolean dir)
	{
		isDirectory = dir; 
	}
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
				fldFileList.add(new File(file.toString())); 
			}
		}

		// Prints the total number of
		// matches to standard out.
		void done()
		{
			System.out.println("Matched: " + numMatches);
			if(numMatches == 0)
			{
				
			}
		}

    // Invoke the pattern matching
    // method on each file.
    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
            IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}
}
