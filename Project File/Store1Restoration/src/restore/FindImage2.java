package restore;


/**
 * The following class finds the icon associated with each file type at dimensions of 80x80
 * @author Michael Pierre
 *
 */
public class FindImage2 
{
	private String fileType_; 
	
	// Returns the string associated with each file type
	public String getIconStr()
	{
		fileType_ = fileType_.toLowerCase();
		if(fileType_.equals("xlsx") || fileType_.equals("xls") || fileType_.equals("csv"))
		{
			return "/fileImage/rsz_rsz_1microsoft_excel_2013_logosvg.png";
		}
		else if(fileType_.equals("pdf"))
		{
			return "/fileImage/rsz_rsz_pdficon.png";
		}
		else if(fileType_.equals("mdb") || fileType_.equals("accdb"))
		{
			return "/fileImage/rsz_rsz_msaccess.png";
		}
		else if(fileType_.equals("docx") || fileType_.equals("doc") || fileType_.equals("rtf"))
		{
			return "/fileImage/rsz_rsz_wordicon.png";
		}
		else if(fileType_.equals("bat") || fileType_.equals("sh"))
		{
			return "/fileImage/rsz_rsz_shellscripticon.png";
		}
		else if(fileType_.equals("c") || fileType_.equals("class") || fileType_.equals("h") || fileType_.equals("xml"))
		{
			return "/fileImage/rsz_rsz_codeicon.png";
		}
		else if(fileType_.equals("java") || fileType_.equals("jar"))
		{
			return "/fileImage/rsz_rsz_javaicon.png";
		}
		else if(fileType_.equals("sql"))
		{
			return "/fileImage/rsz_rsz_sqlicon.jpg";
		}
		else if(fileType_.equals("ora"))
		{
			return "/fileImage/rsz_rsz_oracleicon.jpg";
		}
		else if(fileType_.equals("") || fileType_.equals(" "))
		{
			return "/fileImage/rsz_rsz_foldericon.png";
		}
		else if(fileType_.equals("ppt") || fileType_.equals("pptx"))
		{
			return "/fileImage/rsz_rsz_powerpointicon.png";
		}
		else
		return "/fileImage/rsz_1rsz_fileicon.png";
	}
	public void setFileType(String fileType)
	{
		fileType_ = fileType; 
	}
}
