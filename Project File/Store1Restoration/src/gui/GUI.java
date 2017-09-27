package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Desktop;
import net.miginfocom.swing.MigLayout;
import restore.DateLabelFormatter;
import restore.DirectoryCopy;
import restore.FindImage;
import restore.Restore;

import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JRadioButton;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.Color;

/**
 * The following class is the Graphical user interface for the Store 1 Backup Utility Companion. It contains several methods that satisfy various program
 * needs. GUI also invokes InfoGui which contains the file history window.
 * @author Michael Pierre
 *
 */
public class GUI 
{
	private List<File> fileList = new ArrayList<File>();
	private List<File> fileList2 = new ArrayList<File>();
	private String [] restorePath;
	public JFrame frmStoreSync;
	private JTextField search;
	private JTextArea textArea;
	private JScrollPane scrollPane_1;
	private JList list ;
	private Thread runner;
	private String restoreP;
	private JButton btnRestore;
	private List<Date> dirDateList;
	private File file; 
    private Date beginningDate = null;
	private Restore r;
	private JButton btnSearch;
	private String word;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	private DefaultListModel<String> pathModel;
	private DefaultListModel<String> missingModel;
	private JButton btnOpen;
	private JLabel loading;
	private JTextField dirField;
	private JButton btnViewLog;
	private JLabel lblFrom;
	private JLabel lblTo;
	public JButton btnGo;
	JRadioButton rdbtnFile ;
	List<Date> dList ;
	JRadioButton rdbtnFolder;
	List<String> sList;
	JDatePickerImpl datePicker1;
	JDatePickerImpl datePicker2;
	Date range1, range2;
	String []backupPath;
	private JList pathList;
	JScrollPane scrollPane_4;
	private JLabel lblHello;
	JButton btnHistory;
	private JLabel fileIcon;
	private JButton btnBackupRecovery;
	JFileChooser chooser2 ;
	String store1Path;
	/**
	 * Launch the application
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try
				{
					GUI window = new GUI();
					window.frmStoreSync.setVisible(true);				
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});	
	}

	/**
	 * Create the application.
	 */
	public GUI() 
	{
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	 public void initialize() 
	 {	
		setBackupPath();
		ButtonGroup group = new ButtonGroup();
		pathModel = new DefaultListModel<String>();
		frmStoreSync = new JFrame();
		frmStoreSync.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 14));
		frmStoreSync.setTitle("Store 1 Sync Companion Utility - Ministry of Transportation");
		frmStoreSync.setBounds(100, 100, 1090, 751);
		frmStoreSync.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStoreSync.getContentPane().setLayout(null);
		//Creates restore object
    	r = new Restore(); 
    	//Date Picker Start
		fileIcon = new JLabel("");
		fileIcon.setBounds(782, 67, 158, 226);
		frmStoreSync.getContentPane().add(fileIcon);
    	UtilDateModel model = new UtilDateModel();
    	Properties p = new Properties();
    	p.put("text.today", "Today");
    	p.put("text.month", "Month");
    	p.put("text.year", "Year");
    	JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker1 = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker1.setBounds(86,78,175,32);
    	frmStoreSync.getContentPane().add(datePicker1);
    	btnHistory = new JButton("History");
    	btnHistory.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent arg0)
    		{
    			Object selected = list.getSelectedValue();
				int index = list.getSelectedIndex();
    			InfoGui gui = new InfoGui(rdbtnFolder.isSelected());
    			gui.isDirectory(rdbtnFolder.isSelected());
    			List<File> tmpList = new ArrayList<File>(); 
    			
    			for(int i = 0; i< fileList.size();i++)
    			{
    				if(fileList.get(i).getName().equals(fileList.get(index).getName()))
    				{
    					tmpList.add(fileList.get(i));
    				}
    			}
    			gui.getHistory(tmpList);
    			//new InfoGui().setVisible(true); // Main Form to show after the Login Form..
    		}
    	});
    	btnHistory.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_book-xxl.png")));
		btnHistory.setMargin(new Insets(2, 0, 2, 0));
		btnHistory.setBounds(303, 366, 89, 23);
		frmStoreSync.getContentPane().add(btnHistory);
    	//Date Picker End
    	UtilDateModel model2 = new UtilDateModel();
    	Properties p2 = new Properties();
    	p2.put("text.today", "Today");
    	p2.put("text.month", "Month");
    	p2.put("text.year", "Year");
    	JDatePanelImpl datePanel2 = new JDatePanelImpl(model2,p2);
        datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
    	datePicker2.setBounds(86,127,175,32);
    	frmStoreSync.getContentPane().add(datePicker2);
		JLabel lblPath = new JLabel("Path");
		lblPath.setBounds(530, 370, 46, 14);
		frmStoreSync.getContentPane().add(lblPath);
    	lblHello = new JLabel("");
    	lblHello.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_2icon-ontario.png")));
		lblHello.setBounds(1022, 26, 105, 34);
		frmStoreSync.getContentPane().add(lblHello);
    	search = new JTextField();
    	search.setBackground(UIManager.getColor("Button.highlight"));
		search.setBounds(39, 47, 222, 20);
		frmStoreSync.getContentPane().add(search);
		search.setColumns(10);		
		btnSearch = new JButton("");
		btnSearch.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/dir.png")));
		btnSearch.setBounds(271, 47, 23, 20);
		btnSearch.addActionListener(new ActionListener() 
		{
				public void actionPerformed(ActionEvent ae) 
				{				
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						int option = chooser.showOpenDialog(frmStoreSync);
						if (option == JFileChooser.APPROVE_OPTION)
						{
							search.setText(((chooser.getSelectedFile()!=null)?
										       chooser.getSelectedFile().getAbsolutePath():"nothing"));
						}	 
				}
		});	
		frmStoreSync.getContentPane().add(btnSearch);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(325, 67, 342, 226);
		frmStoreSync.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(textArea);
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		getAllDirectories();
		setInitialDate();

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 400, 510, 301);
		list = new JList<String>(listModel);
		list.setBackground(new Color(255, 255, 255));
		scrollPane_1.setViewportView(list);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		list.setVisible(true);
		list.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{			
					int index = list.getSelectedIndex();
					FindImage fi = new FindImage();

					try
					{
						if(fileList.get(index).isFile())
						{
							String [] getFileType = fileList.get(index).getName().split("\\.");
							String fileType = getFileType[getFileType.length-1];
							System.out.println("TYPE   " + fileType);
							fi.setFileType(fileType); 
						}
						else
				    	fi.setFileType("");
				    	fileIcon.setIcon(new ImageIcon(InfoGui.class.getResource(fi.getIconStr())));
				    	
					}
					catch(Exception e)
					{
						
					}
			}
		});
		list.addMouseListener(new MouseAdapter() 
		{
		    public void mouseClicked(MouseEvent evt)
		    {
		    	  if(evt.getClickCount() == 2)
		    	  {
		    		  if(rdbtnFile.isSelected())
		    		  {
		    		     int index = list.locationToIndex(evt.getPoint());
		    		     ListModel dlm = list.getModel();
		    		     Object item = dlm.getElementAt(index);
		    		     System.out.println("INDEX       " + index);
		    		     System.out.println("Selected " + item.toString());
		    		     list.ensureIndexIsVisible(index);
		    		     System.out.println("Double clicked on " + item.toString() + " FILE LIST LENGTH " + fileList.size());

		    		     try 
		    			 {
		    				 System.out.println("File Path " + fileList.get(index).getAbsolutePath());
		    				 // System.out.println("PARENT " + file.getParentFile());
		    				 Desktop.getDesktop().open(fileList.get(index).getParentFile());
		    			 }   	    
		    			 catch (IOException e) 
		    			 {
		    				 e.printStackTrace();
		    			 }
		    		  }
		    		  else
		    		  {
			    		     int index = list.locationToIndex(evt.getPoint());
			    		     try 
			    			 {
			    				 Desktop.getDesktop().open(fileList.get(index).getParentFile());
			    			 }   	    
			    			 catch (IOException e) 
			    			 {
			    				 e.printStackTrace();
			    			 }		    		  
		    		  }
		    	 }
		    }
		});
		
		missingModel = new DefaultListModel<String>();

		btnRestore = new JButton("Restore");
		btnRestore.setMargin(new Insets(2, 0, 2, 0));
		btnRestore.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_restore.png")));
		btnRestore.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int index = list.getSelectedIndex();
				btnRestore.setEnabled(false);
				textArea.setCaretPosition(textArea.getDocument().getLength());
				try
				{	getMyDocuments();
					System.out.println("RESTORE P    " + restoreP);
					File newFile = new File(restoreP + "/" +fileList.get(index).getName());
				
					System.out.println("New file:   " + newFile.getAbsolutePath());
					chooser2 = new JFileChooser(getMyDocuments())
					{
						@Override
					    public void approveSelection()
					 	{
							File check = new File(chooser2.getSelectedFile().getAbsolutePath() + "\\" + fileList.get(index).getName());
							System.out.println("Chooser    " + check.getAbsolutePath());
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
					int option = chooser2.showSaveDialog(frmStoreSync);
					boolean work = false;
				
					if (option == JFileChooser.APPROVE_OPTION)
					{	
						show("Restoring....");
						restoreP = chooser2.getSelectedFile().getAbsolutePath().replace("\\", "/");
						restoreP = restoreP + "/";
						System.out.println("Restore point " + restoreP);
						work = true;
					}
					if(work)
					{
						System.out.println("FILE LIST SIZE " + fileList.size());
						r.setGuiFileList(fileList);
						Object selected = list.getSelectedValue();
						
						if(rdbtnFile.isSelected())
						{
							if(selected != null)
							{
		    			        File dest = new File(restoreP + fileList.get(index).getName());
		    		         
		    			        try 
		    			        {
		    			        	FileUtils.copyFile(fileList.get(index).getAbsoluteFile(), dest);
		    			        } 
		    			        catch (IOException e1) 
		    			        {
		    			        	e1.printStackTrace();
		    			        }
						
		    			        try 
		    			        {
		    			        	String s = dest.getParentFile().toString();
		    			        	Desktop.getDesktop().open(new File(s));
		    			        }   	    
		    			        catch (IOException e1) 
		    			        {
		    			        	e1.printStackTrace();
		    			        }
		    					
		    			        btnRestore.setEnabled(true);

							}
						}	
						else if(rdbtnFolder.isSelected())
						{
							System.out.println("Folder Restore");
							DirectoryCopy dc = new DirectoryCopy(); 
							File dest = new File(restoreP + fileList.get(index).getName());
							dc.setSource(fileList.get(index).getAbsolutePath());
							dc.setDestination(dest.getAbsolutePath());
							dc.copy();
		    		     
							try 
							{
								String s = dest.getParentFile().toString();
								Desktop.getDesktop().open(new File(s));
							}   	    
							catch (IOException e1) 
							{
								e1.printStackTrace();
							}
							btnRestore.setEnabled(true);

						}
						
						btnRestore.setEnabled(true);
						 try 
			    		 {
			    			Desktop.getDesktop().open(new File(restoreP));
			 				btnRestore.setEnabled(true);

			    		 }   	    
						 catch (IOException e1) 
						 {
			    				 e1.printStackTrace();
						 }
						show("Complete!");
					}
					else
						btnRestore.setEnabled(true);

				
					btnRestore.setEnabled(true);

				}
			
				catch(Exception e1)
				{
					show("ERROR: No file/folder selected " + e1.getMessage());
					btnRestore.setEnabled(true);

				}
			}
		});
				btnRestore.setBounds(102, 366, 98, 23);
				frmStoreSync.getContentPane().add(btnRestore);
				
				JScrollPane scrollPane_2 = new JScrollPane();
				scrollPane_2.setBounds(445, 355, -53, 20);
				frmStoreSync.getContentPane().add(scrollPane_2);
				
				JLabel lblSearchResults = new JLabel("Search Results");
				lblSearchResults.setBounds(10, 370, 121, 14);
				frmStoreSync.getContentPane().add(lblSearchResults);
				
				JLabel lblMessageBox = new JLabel("Message Box");
				lblMessageBox.setBounds(329, 50, 132, 14);
				frmStoreSync.getContentPane().add(lblMessageBox);
				
				btnOpen = new JButton("Open");
				btnOpen.setMargin(new Insets(2, 0, 2, 0));
				btnOpen.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_openfile.png")));
				// Opens file or folder. If file than it will open the file with the appropriate software if applicable. If folder than it will open 
				// the folders location
				btnOpen.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						
							Object item= list.getSelectedValue();
			    		    ListModel dlm = list.getModel();
			    		    System.out.println("Double clicked on " + item);
			    		    int index = list.getSelectedIndex();

			    			try 
			    			{
			    				 Desktop.getDesktop().open(new File(fileList.get(index).getAbsolutePath()));
			    		    }   	    
			    			catch (IOException e) 
			    			{
			    				 e.printStackTrace();
			    			}

					}
				});
				
				btnOpen.setBounds(215, 366, 78, 23);
				frmStoreSync.getContentPane().add(btnOpen);
				dirField = new JTextField();
				dirField.setBackground(UIManager.getColor("Button.highlight"));
				dirField.setBounds(39, 237, 222, 20);
				frmStoreSync.getContentPane().add(dirField);
				dirField.setColumns(10);				
				JButton btnClear = new JButton("Clear");
				btnClear.setMargin(new Insets(2, 0, 2, 0));
				btnClear.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_redx.png")));
				btnClear.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						textArea.setText("");
						textArea.setText("Welcome to the Store 1 Companion Backup Utility\n");
							show("\n"+"Current Backup Range");
						    show(beginningDate.toString());
						    show("To");
						    show(dirDateList.get(dirDateList.size() -1).toString());
						    show("________________________________________________________");
					}
				});
			
				btnClear.setBounds(455, 299, 77, 23);
				frmStoreSync.getContentPane().add(btnClear);
				JButton btnDir = new JButton("");
				btnDir.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{										
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					    chooser.setMultiSelectionEnabled(true);
					    List<String> dirList = new ArrayList<String>();
						int option = chooser.showOpenDialog(frmStoreSync);
						if (option == JFileChooser.APPROVE_OPTION)
						{					
							File  [] selectedFiles = chooser.getSelectedFiles();
							String s = "";
							if(selectedFiles.length > 1)
							{
								for(int x = 0; x < selectedFiles.length;x++)
								{
									s = s.concat(selectedFiles[x].getAbsolutePath() + "|");
								}
								dirField.setText(((s!=null)?s:"nothing"));
						
								String [] splitDir = s.split("\\|");
								for(int i = 0; i < splitDir.length;i++)
								{
									dirList.add(splitDir[i]); 
									System.out.println(splitDir[i]);
								}
							}
							else
							{
								dirField.setText(((s!=null)?selectedFiles[0].getAbsolutePath():"nothing"));								
								dirList.add(selectedFiles[0].getAbsolutePath());
							}
							r.setDirList(dirList);
						}
					}
				});
				btnDir.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/dir.png")));
				btnDir.setBounds(270, 237, 23, 20);
				frmStoreSync.getContentPane().add(btnDir);
				
				JLabel lblFileSearch = new JLabel("Search");
				lblFileSearch.setBounds(39, 31, 77, 14);
				frmStoreSync.getContentPane().add(lblFileSearch);
				
				JLabel lblBackupUtilityCompanion = new JLabel("Backup Utility Companion V3");
				lblBackupUtilityCompanion.setFont(new Font("Segoe UI Light", Font.ITALIC, 21));
				lblBackupUtilityCompanion.setBounds(782, 0, 282, 42);
				frmStoreSync.getContentPane().add(lblBackupUtilityCompanion);
				
				btnViewLog = new JButton("View Log");
				btnViewLog.setMargin(new Insets(2, 0, 2, 0));
				btnViewLog.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_text.png")));
				btnViewLog.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						    BufferedReader in = null;
							
					    	try 
					    	{
					    		in = new BufferedReader(new FileReader("LogLocation/LogLocation.txt"));
					    	} 
					    	catch (FileNotFoundException e1) 
					    	{
					    		e1.printStackTrace();
					    	}
						    String str;
						    List<String> list = new ArrayList<String>();
						    try 
						    {
						    	while((str = in.readLine()) != null)
						    	{
							       list.add(str);
						    	}
						    } 
						    catch (IOException e1) 
						    {
						    	e1.printStackTrace();
						    }
						   
						    String []  logPath = list.toArray(new String[0]);
						    File tmpFile = new File(logPath[0]);
						    try 
						    {
			    				 System.out.println("File Path " + tmpFile.getAbsolutePath());
			    				 Desktop.getDesktop().open(new File(tmpFile.getAbsolutePath()));
			    			}   	    
			    			catch (IOException e1) 
			    			{
			    				e1.printStackTrace();
			    			}						
					}
				});
				btnViewLog.setBounds(402, 366, 101, 23);
				frmStoreSync.getContentPane().add(btnViewLog);
				
				JLabel lblNewLabel_1 = new JLabel("By: Michael Pierre");
				lblNewLabel_1.setFont(new Font("Segoe UI Light", Font.ITALIC, 13));
				lblNewLabel_1.setBounds(802, 36, 101, 14);
				frmStoreSync.getContentPane().add(lblNewLabel_1);
				
				lblFrom = new JLabel("From:");
				lblFrom.setBounds(39, 78, 46, 14);
				frmStoreSync.getContentPane().add(lblFrom);

				lblTo = new JLabel("To:");
				lblTo.setBounds(39, 126, 46, 14);
				frmStoreSync.getContentPane().add(lblTo);
				
				btnGo = new JButton("Search");
				btnGo.setMargin(new Insets(2, 0, 2, 0));

				btnGo.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/searchIcon.png")));
				frmStoreSync.getRootPane().setDefaultButton(btnGo);
		
				btnGo.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{  
						btnGo.setEnabled(false);
						pathModel.clear();
						textArea.setCaretPosition(textArea.getDocument().getLength());
						show("Searching....");
						Date d1 = null; 
						Date d2 = null; 
						List<String> sListNew = new ArrayList<String>();
					    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					
					    if(dirField.getText().equals(""))
					    {
					    	try 
					    	{
					    		d1 = sdf2.parse(datePicker1.getJFormattedTextField().getText());
					    	} 
					    	catch (ParseException e) 
					    	{
					    		show("ERROR");
								btnGo.setEnabled(true);
								textArea.setCaretPosition(textArea.getDocument().getLength());
					    		e.printStackTrace();
					    	}
						
					    	try 
					    	{
					    		d2 = sdf2.parse(datePicker2.getJFormattedTextField().getText());
					    	} 
					    	catch (ParseException e) 
					    	{
					    		show("ERROR");
								btnGo.setEnabled(true);
								textArea.setCaretPosition(textArea.getDocument().getLength());
					    		e.printStackTrace();
					    	}
						
					    	if(datePicker1.getJFormattedTextField().getText().equals("") || datePicker1.getJFormattedTextField().getText().equals("") )
					    	{
								btnGo.setEnabled(true);
								textArea.setCaretPosition(textArea.getDocument().getLength());
					    		show("ERROR");
					    	}
						
					    	else if(d1.after(d2))
					    	{
					    		show("ERROR: 'From' date must be before 'to' date");
								btnGo.setEnabled(true);
					    	}
						
					    	else if(d1.before(range1) || d2.after(range2))
					    	{
					    		show("ERROR: Out of Range");
								btnGo.setEnabled(true);
					    	}
					    	else
					    	{
								textArea.setCaretPosition(textArea.getDocument().getLength());
					    		show("Selected from: " + d1 + " - " + d2 );								
					    		System.out.println("Size " + dList.size() + " sList " + sList.size());
					    		List<String> tmpList = new ArrayList<String>();
					    		List<String> tmpStr = new ArrayList<String>();
					    		List<Date> tmpD = new ArrayList<Date>();
					    		List<String> dirList = new ArrayList<String>();
						    
					    		for(int s=0;s< dList.size();s++)
					    		{								
					    			tmpStr.add(sdf2.format(dList.get(s)).toString());
					    		}
							
					    		System.out.println(tmpStr.get(0));
					    		Date d = null;
					    		for(int s=0;s< dList.size();s++)
					    		{
					    			try 
					    			{
					    				d = sdf2.parse(tmpStr.get(s));
					    			} 
					    			catch (ParseException e) 
					    			{
					    				e.printStackTrace();
					    			}
					    			tmpD.add(d);								
					    		}
							
					    		sListNew.addAll(sList);
					    		System.out.println("DLIST ONE " + dList.get(0) + " SLIST ONE " + sList.get(0));
								sListNew.add(0, sListNew.get(sListNew.size()-1));
								System.out.println("DLIST ONE " + dList.get(0) + " SLIST ONE " + sList.get(0));
								System.out.println("Size " + dList.size() + " Size 2 " + sList.size());
								sListNew.remove(sListNew.size() - 1);						    
								System.out.println("TEMP SIZE " + tmpD.get(0));
								
								for(int x = 0; x < dList.size();x++)
								{
									System.out.println("Comparing  " + tmpD.get(x) + "  TO  " + d1  );
									if((tmpD.get(x).before(d1) && !tmpD.get(x).equals(d1)) || (tmpD.get(x).after(d2)))
									{
										System.out.println("ADDING " + sListNew.get(x) );
										tmpList.add(sListNew.get(x));
									}													
								}
							
								for(int ii = 0; ii< tmpList.size();ii++)
								{
									System.out.println("tmpList  " + tmpList.get(ii));
								}
													
								sListNew.removeAll(tmpList);
								for(int z = 0;z < sListNew.size();z++)
								{
									System.out.println("File: " + sListNew.get(z));
									dirList.add(backupPath[0]+"/"+sListNew.get(z));
								}				

								if(rdbtnFile.isSelected())
								{
									if(!search.getText().contains(":"))
									{
										r.setDirList(dirList);
										textArea.setCaretPosition(textArea.getDocument().getLength());
										EventQueue.invokeLater(new Runnable() 
										{
											public void run() 
											{
												SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
												//btnFind.setEnabled(false);
												listModel.removeAllElements();
									
												if(!search.getText().equals(""))
													r.searchFile(search.getText());
									
												else 
													show("ERROR: No search input"); 	btnGo.setEnabled(true);

									
												System.out.println("GUI File list size " + fileList2.size());
												
												for(int d = 0;d < fileList.size();d++)
												{
													listModel.addElement(d+". "+fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
													pathModel.addElement(d+". "+fileList.get(d).getAbsolutePath());
												}
			
											    //btnFind.setEnabled(true);
					    						list.setVisible(true);
					    						textArea.append("Complete!" + "\n");							
											}
										});	
									}
									else if(search.getText().contains(":"))
									{
										textArea.setCaretPosition(textArea.getDocument().getLength());
										File tmpFile = new File(search.getText());
										r.setDirList(dirList);
										EventQueue.invokeLater(new Runnable() 
										{
											public void run() 
											{
												SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
											//	btnFind.setEnabled(false);
												listModel.removeAllElements();
									
												if(!search.getText().equals(""))
													r.searchFile(tmpFile.getName());
									
												else 
													show("ERROR: No search input"); 	btnGo.setEnabled(true);
									
												System.out.println("GUI File list size " + fileList2.size());
					    				
												for(int d = 0;d < fileList.size();d++)
												{
													listModel.addElement(d+". "+fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
													pathModel.addElement(d+". "+fileList.get(d).getAbsolutePath());

												}
												list.setVisible(true);
												textArea.append("Complete!" + "\n");							
											}
										});	
										
									}
					    	}
					    	else if(rdbtnFolder.isSelected())
					    	{	
								textArea.setCaretPosition(textArea.getDocument().getLength());
					    		r.setDirList(dirList);
					    		EventQueue.invokeLater(new Runnable() 
								{
									public void run() 
									{
										SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										listModel.removeAllElements();
										
										if(!search.getText().equals(""))
											r.searchFolder(search.getText());
											
										else 
											show("ERROR: No search input");	btnGo.setEnabled(true);


										
										System.out.println("GUI File list size " + fileList2.size());
										for(int d = 0;d < fileList.size();d++)
										{
											listModel.addElement(d+". "+fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
											pathModel.addElement(d+". "+fileList.get(d).getAbsolutePath());
										}
										
										list.setVisible(true);
										textArea.append("Complete!" + "\n");
									}
								});	
					    	}
						}
					}
					else if(!dirField.getText().equals(""))
					{
						textArea.setCaretPosition(textArea.getDocument().getLength());
						if(rdbtnFile.isSelected())
						{
							if(!search.getText().contains(":"))
							{
								EventQueue.invokeLater(new Runnable() 
								{
									public void run() 
									{
										SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										listModel.removeAllElements();
							
										if(!search.getText().equals(""))
											r.searchFile(search.getText());
							
										else 
											show("ERROR: No search input");
							
										System.out.println("GUI File list size " + fileList2.size());
			    				
										for(int d = 0;d < fileList.size();d++)
										{
											listModel.addElement(d+". "+fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
											pathModel.addElement(d+". "+fileList.get(d).getAbsolutePath());
										}	
										list.setVisible(true);
										textArea.append("Complete!" + "\n");	

									}
									
								});	

							}
							
							else if(search.getText().contains(":"))
							{
								textArea.setCaretPosition(textArea.getDocument().getLength());
								File tmpFile = new File(search.getText());
								EventQueue.invokeLater(new Runnable() 
								{
									public void run() 
									{
										SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										listModel.removeAllElements();
							
										if(!search.getText().equals(""))
											r.searchFile(tmpFile.getName());
							
										else 
											show("ERROR: No search input");
							
										System.out.println("GUI File list size " + fileList2.size());
			    				
										for(int d = 0;d < fileList.size();d++)
										{
											listModel.addElement(d+". "+fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
											pathModel.addElement(d+". "+fileList.get(d).getAbsolutePath());
										}
			    						list.setVisible(true);
			    						textArea.append("Complete!" + "\n");							
									}
								});	
			    			
							}
						}
						else if(rdbtnFolder.isSelected())
						{
							textArea.setCaretPosition(textArea.getDocument().getLength());
							EventQueue.invokeLater(new Runnable() 
							{
								public void run() 
								{
									SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									listModel.removeAllElements();
									
									if(!search.getText().equals(""))
										r.searchFolder(search.getText());
										
									else 
										show("ERROR: No search input");
									
									System.out.println("GUI File list size " + fileList2.size());
									for(int d = 0;d < fileList.size();d++)
									{
										listModel.addElement(fileList.get(d).getName() + "  Last modified, " + sdf.format(fileList.get(d).lastModified()));	
										pathModel.addElement(fileList.get(d).getAbsolutePath());
									}
									
									list.setVisible(true);
									textArea.append("Complete!" + "\n");
									
								}
							});	
						}
					}
					}

				});
				btnGo.setBounds(115, 272, 89, 23);
				frmStoreSync.getContentPane().add(btnGo);
				
				rdbtnFolder = new JRadioButton("Folder");
				rdbtnFolder.setBackground(new Color(248, 248, 255));
				group.add(rdbtnFolder);
				rdbtnFolder.setBounds(191, 166, 70, 23);
				frmStoreSync.getContentPane().add(rdbtnFolder);
				
				rdbtnFile = new JRadioButton("File");
				rdbtnFile.setBackground(new Color(248, 248, 255));
				group.add(rdbtnFile);
				rdbtnFile.setSelected(true);
				rdbtnFile.setBounds(86, 166, 46, 23);
				frmStoreSync.getContentPane().add(rdbtnFile);
				
				JLabel lblCustomDirectory = new JLabel("Custom Directory/Directories");
				lblCustomDirectory.setBounds(39, 212, 222, 14);
				frmStoreSync.getContentPane().add(lblCustomDirectory);
				
			    scrollPane_4 = new JScrollPane();
				scrollPane_4.setBounds(530, 400, 534, 301);
				//BoundedRangeModel model3 = scrollPane_1.getVerticalScrollBar().getModel();
				//scrollPane_4.getVerticalScrollBar().setModel(model3);
				frmStoreSync.getContentPane().add(scrollPane_4);
				
				pathList = new JList<String>(pathModel);
				pathList.setBackground(new Color(255, 255, 255));
				scrollPane_4.setViewportView(pathList);
				pathList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				frmStoreSync.getContentPane().add(scrollPane_1);	
				btnBackupRecovery = new JButton("Backup Recovery");
				btnBackupRecovery.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						BackupRecoveryGui br = new BackupRecoveryGui(range1, range2);
						br.setDList(dList);
						br.setSList(sList);
					}
				});
				
				
			
				
				btnBackupRecovery.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/rsz_backupicon.png")));
				btnBackupRecovery.setMargin(new Insets(2, 0, 2, 0));
				btnBackupRecovery.setBounds(927, 366, 137, 23);
				frmStoreSync.getContentPane().add(btnBackupRecovery);
				JLabel label = new JLabel("");
				label.setBackground(new Color(248, 248, 255));
				label.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/background.jpg")));
				label.setBounds(0, 0, 1074, 713);
				frmStoreSync.getContentPane().add(label);
				
				
	}
	public void setFile(File file1)
	{
		file = file1;
	}
	// Shows messages in the message box in the GUI
	public void show(String text)
	{
		textArea.append(text + "\n");
	}
	
	public void setFileList(List<File> fl)
	{	System.out.println("setFileList " + fl.size());
		fileList = fl;
	}
	
	
	
	// The following method sets the location of the backup to be later used to search and restore from
	// Works by reading from file to array with the first element being the backup path.
	public void setBackupPath()
	{
		BufferedReader in = null;
		
    	try 
    	{
    		in = new BufferedReader(new FileReader("BackupPath/BackupPath.txt"));
    	} 
    	catch (FileNotFoundException e1) 
    	{
    		e1.printStackTrace();
    	}
	    String str;

	    List<String> list = new ArrayList<String>();
	    try 
	    {
	    	while((str = in.readLine()) != null)
	    	{
		       list.add(str);
	    	}
	    } 
	    catch (IOException e1) 
	    {
	    	e1.printStackTrace();
	    }
	    backupPath = list.toArray(new String[0]);
	}
	public void setFileList2(List<File> fl2)
	{
		fileList2 = fl2; 
	}
	
	public void load()
	{
		loading = new JLabel("");
		loading.setIcon(new ImageIcon(GUI.class.getResource("/fileImage/preload.gif")));
		loading.setBounds(20, 392, 79, 80);
		loading.setVisible(true);
		frmStoreSync.getContentPane().add(loading);
	}
	
	// The following method sets the initial date for the search. The initial dates "from" and "to" dates are the range of the whole backup
	public void setInitialDate()
	{
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String initialDate = sdf.format(range1);
			System.out.println("Initial Date   " + initialDate);
			String [] initialDateStr = initialDate.split("/");
			String initialDate2 = sdf.format(range2); 
			String [] initialDateStr2 = initialDate2.split("/");
		
		    int month1 = Integer.parseInt(initialDateStr[0]);
		    int day1 = Integer.parseInt(initialDateStr[1]);
		    int year1 = Integer.parseInt(initialDateStr[2].substring(0, 4));
			datePicker1.getModel().setMonth(month1-1);
	        datePicker1.getModel().setDay(day1);
	        datePicker1.getModel().setYear(year1);
	        datePicker1.getModel().setSelected(true);
	        
	        int month2 = Integer.parseInt(initialDateStr2[0]);
	        int day2 = Integer.parseInt(initialDateStr2[1]);
	        int year2 = Integer.parseInt(initialDateStr2[2].substring(0,4));
	        
	        datePicker2.getModel().setMonth(month2-1);
	        datePicker2.getModel().setDay(day2);
	        datePicker2.getModel().setYear(year2);
	        datePicker2.getModel().setSelected(true);
	}
	
	// The following methods finds the users "My Documents" folder and uses it to create a default restored files directory that is used to restore 
	// user specified files or folders.
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
	    System.out.println(fw.getDefaultDirectory().toString());
	    restoreP = dir.getAbsolutePath();
	    return dir.getAbsolutePath();
	}
	
	// The following method gets all the directories in the store 1 backup. It then creates a directory list and a date list used to capture the 
	// range of the backup.
	public void getAllDirectories()
	{			
		show("Welcome to the Store 1 Companion Backup Utility");

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
	    dirDateList = new ArrayList<Date>();
		List<String> strDateList = new ArrayList<String>();
	    
	    DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>()
	    {
	        @Override
	        public boolean accept(Path file) throws IOException 
	        {
	            return (Files.isDirectory(file));
	        }
	    };

	    Path dir = null;
	    try
	    {
	    	dir = FileSystems.getDefault().getPath(backupPath[0]);
	    }
	    catch(Exception e) 
	    {
	    	System.out.println("ERROR: Directory not found");
	    }
	    sList = new ArrayList<String>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) 
	    {
	        for (Path path : stream) 
	        {
	            // Iterate over the paths in the directory
	        	strDateList.add(path.getFileName().toString());
	        	sList.add(path.getFileName().toString());
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    
	    if(strDateList.size()>0)
	    {
	    	System.out.println("STRING LIST SIZE " + strDateList.size());
	    	System.out.println("START: " + strDateList.get(strDateList.size() - 1));
	    	String beginning = strDateList.get(strDateList.size() - 1);
	    	strDateList.remove(strDateList.get(strDateList.size() - 1));
	    	beginning = beginning.substring(beginning.indexOf("_") + 1);
	    	beginning = beginning.substring(0, beginning.indexOf("_"));
	    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

	    	System.out.println(beginning);
	    	try 
	    	{
	    		beginningDate = sdf2.parse(beginning);
	    	}
	    	catch (ParseException e)
	    	{
	    		e.printStackTrace();
	    	}
	    
	    	dirDateList.add(beginningDate);
	    	for(int i = 0; i < strDateList.size();i++)
	    	{
	    		Date tmpDate = null;
	    		try 
	    		{
	    			tmpDate = sdf.parse(strDateList.get(i));
	    		} 
	    		catch (ParseException e) 
	    		{
	    			e.printStackTrace();
	    		}
	    		dirDateList.add(tmpDate);
	    	}
	     
	    	show("\n"+"Current Backup Range");
	    	show(beginningDate.toString());
	    	show("To");
	    	show(dirDateList.get(dirDateList.size() -1).toString());
	    	show("________________________________________________________");
	    	range1 = beginningDate; 
	    	range2 = dirDateList.get(dirDateList.size() - 1);
	    	dList = dirDateList; 
	    }
	    else if(strDateList.size()<=0)
	    {
	    	show("ERROR: Backup directory not found! Please use config file to point to working backup directory.");
	    }
	}
}