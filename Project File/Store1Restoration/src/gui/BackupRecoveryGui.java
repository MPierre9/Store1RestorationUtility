package gui;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import restore.DateLabelFormatter;
import restore.DirectoryCopy;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * The BackupRecoveryGui class handles all operations needed to create a recovery image between two date ranges. 
 * @author Michael Pierre
 *
 */
public class BackupRecoveryGui  implements Runnable
{
	private JFrame frmBackupRecovery;
	JDatePickerImpl datePicker1;
	JDatePickerImpl datePicker2;
	JButton btnRecover;
	Date range1,range2;
	private JLabel lblFrom;
	private JLabel lblTo;
	List<Date> dList ;
	private Thread runner;
	List<String> sList;
	String restoreP; 
	JTextArea textArea;
	private JLabel lblLoad;
	Date d1 ;
	Date d2 ;
	JFileChooser chooser1;
	private JButton btnClear;
	String [] backupPath;
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
					BackupRecoveryGui window = new BackupRecoveryGui();
					window.frmBackupRecovery.setVisible(true);
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
	public BackupRecoveryGui()
	{
		initialize(); //remember to remove when done
	}

	public BackupRecoveryGui(Date r1, Date r2)
	{
		range1 = r1; 
		range2 = r2;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize()
	{
		setBackupPath();
		frmBackupRecovery = new JFrame();
		frmBackupRecovery.setTitle("Backup Recovery");
		frmBackupRecovery.setBounds(100, 100, 360, 490);
		frmBackupRecovery.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBackupRecovery.getContentPane().setLayout(null);
		getMyDocuments();
		UtilDateModel model = new UtilDateModel();
    	Properties p = new Properties();
    	p.put("text.today", "Today");
    	p.put("text.month", "Month");
    	p.put("text.year", "Year");
    	JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker1 = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker1.setBounds(75,60,175,32);
    	frmBackupRecovery.getContentPane().add(datePicker1);
    	UtilDateModel model2 = new UtilDateModel();
    	Properties p2 = new Properties();
    	p.put("text.today", "Today");
    	p.put("text.month", "Month");
    	p.put("text.year", "Year");
    	JDatePanelImpl datePanel2 = new JDatePanelImpl(model2,p2);
        datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        datePicker2.setBounds(75,138,175,32);
    	frmBackupRecovery.getContentPane().add(datePicker2);
    	
		btnRecover = new JButton("Recover");
		btnRecover.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				chooser1 = new JFileChooser(getMyDocuments())
				{
				
					@Override
				    public void approveSelection()
				 	{
						File check = new File(chooser1.getSelectedFile().getAbsolutePath() + "\\" + "Store1_Recovery");

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
				int option = chooser1.showSaveDialog(frmBackupRecovery);
				boolean work = false;
			
				if (option == JFileChooser.APPROVE_OPTION)
				{	
					show("Restoring....");
					restoreP = chooser1.getSelectedFile().getAbsolutePath().replace("\\", "/");
					restoreP = restoreP + "/";
					System.out.println("Restore point " + restoreP);
					work = true;
				}
				
				
				if(work)
				{
					btnRecover.setEnabled(false);
					lblLoad.setVisible(true);
					List<String> sListNew = new ArrayList<String>();	
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					try 
					{
						d1 = sdf2.parse(datePicker1.getJFormattedTextField().getText());
					} 
			    	catch (ParseException e1) 
			    	{
			    		show("Error: " + e1);
			    		lblLoad.setVisible(false);
			    		textArea.setCaretPosition(textArea.getDocument().getLength());
			    		e1.printStackTrace();
			    	}
			
					try 
					{
						d2 = sdf2.parse(datePicker2.getJFormattedTextField().getText());
					} 
					catch (ParseException e1) 
					{
						lblLoad.setVisible(false);
						show("Error: " + e1);
						textArea.setCaretPosition(textArea.getDocument().getLength());
						e1.printStackTrace();
					}
		    
					if(datePicker1.getJFormattedTextField().getText().equals("") || datePicker1.getJFormattedTextField().getText().equals("") )
					{
						btnRecover.setEnabled(true);
						textArea.setCaretPosition(textArea.getDocument().getLength());
						show("ERROR");
						lblLoad.setVisible(false);
					}
			
					else if(d1.after(d2))
					{
						show("ERROR: 'From' date must be before 'to' date");
						btnRecover.setEnabled(true);
						lblLoad.setVisible(false);
					}
			
					else if(d1.before(range1) || d2.after(range2))
					{
						show("ERROR: Out of Range");
						btnRecover.setEnabled(true);
						lblLoad.setVisible(false);
					}
					else
					{
						show("Selected from: " + d1 + " - " + d2 );								
						System.out.println("Size " + dList.size() + " sList " + sList.size());
						RunnableThread("run");
	    		
						try 
						{
							Desktop.getDesktop().open(new File(restoreP));
							btnRecover.setEnabled(true);

						}   	    
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		btnRecover.setIcon(new ImageIcon(BackupRecoveryGui.class.getResource("/fileImage/rsz_checkmark.png")));
		btnRecover.setBounds(118, 199, 89, 23);
		btnRecover.setMargin(new Insets(2, 0, 2, 0));

		frmBackupRecovery.getContentPane().add(btnRecover);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 233, 324, 208);
		frmBackupRecovery.getContentPane().add(scrollPane);
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textArea.setText("Enter valid date range and click recover." + "\n \n" +"Depending on date range and size recovery could take \nseveral minutes to hours."
				+ "\n ______________________________________________________________");
		
		lblFrom = new JLabel("From:");
		lblFrom.setBounds(19, 60, 46, 14);
		frmBackupRecovery.getContentPane().add(lblFrom);
		lblLoad = new JLabel("load");
		lblLoad.setVisible(false);
		lblLoad.setIcon(new ImageIcon(BackupRecoveryGui.class.getResource("/fileImage/loader.gif")));
		lblLoad.setBounds(214, 199, 25, 23);
		frmBackupRecovery.getContentPane().add(lblLoad);
	
		btnClear = new JButton("");
		btnClear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				textArea.setText("");
				textArea.setText("Enter valid date range and click recover." + "\n \n" +"Depending on date range and size recovery could take \nseveral minutes to hours."
						+ "\n ______________________________________________________________");
			}
		});
		btnClear.setMargin(new Insets(2, 0, 2, 0));

		btnClear.setIcon(new ImageIcon(BackupRecoveryGui.class.getResource("/fileImage/rsz_redx.png")));
		btnClear.setBounds(309, 210, 25, 23);
		frmBackupRecovery.getContentPane().add(btnClear);
		lblTo = new JLabel("To:");
		lblTo.setBounds(19, 136, 46, 14);
		frmBackupRecovery.getContentPane().add(lblTo);
		JLabel lblBackupRecoveryTool = new JLabel("Backup Recovery Tool");
		lblBackupRecoveryTool.setFont(new Font("Segoe UI", Font.ITALIC, 18));
		lblBackupRecoveryTool.setBounds(75, 11, 193, 38);
		frmBackupRecovery.getContentPane().add(lblBackupRecoveryTool);
		JLabel lblBackground = new JLabel("background");
		lblBackground.setIcon(new ImageIcon(BackupRecoveryGui.class.getResource("/fileImage/background.jpg")));
		lblBackground.setBounds(0, 0, 344, 452);
		frmBackupRecovery.getContentPane().add(lblBackground);
		
		setInitialDate();
		frmBackupRecovery.setVisible(true);
	
	}
	
	/**
	 * Sets the initial date to be shown on screen. The initial dates are the date range of the backup
	 */
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
	
	/**
	 * Sets dList which is a date of all backup dates
	 * @param d
	 */
	public void setDList(List<Date> d)
	{
		dList = new ArrayList<Date>(d);
	}
	
	/**
	 * sets sList which a string list of the backup directories
	 * @param s
	 */
	public void setSList(List<String> s)
	{
		sList = new ArrayList<String>(s);
	}
	
	/**
	 * this method locates the users my documents folder and create a subfolder called "RestoredFiles_Store1"
	 * @return the users my documents location
	 */
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
	
	
	public void show(String s)
	{
		textArea.append("\n"+s);
	}

	public void RunnableThread(String threadName)
	{
		runner = new Thread(this, threadName); 
		runner.start();
	}
	
	/**
	 * This thread runs the recovery process
	 */
	@Override
	public void run() 
	{				
		show("Started Recovery...");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> sListNew = new ArrayList<String>();
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
			catch (ParseException e1) 
			{
				e1.printStackTrace();
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
		System.out.println("DATE TO COMPARE    " + d1 +"  and   " + d2);
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
		List<File> fList = new ArrayList<File>(); 
		for(int z = 0;z < sListNew.size();z++)
		{
			System.out.println("File: " + sListNew.get(z));
			fList.add(new File(backupPath[0]+"/"+sListNew.get(z)));
		}		
		File dest = new File(restoreP + "\\Store1_Recovery");
		System.out.println("RESTORE PATH 3   " + restoreP);
		for(int i = 0; i < fList.size();i++)
		{
			DirectoryCopy dc = new DirectoryCopy(); 
			textArea.setCaretPosition(textArea.getDocument().getLength());
			show("Restoring: " + fList.get(i).getName());
			dc.setSource(fList.get(i).getAbsolutePath());
			dc.setDestination(dest.getAbsolutePath());
			dc.copy();
			
		}
		
		lblLoad.setVisible(false);
		btnRecover.setEnabled(true);
		show("Complete!");		
	}
	
	/**
	 * Sets the backup path location
	 */
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
}
