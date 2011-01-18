package com.readytalk.olive.servlet;
//Modified from: http://www.jsptube.com/servlet-tutorials/servlet-file-upload-example.html
// Also see: http://stackoverflow.com/questions/4101960/storing-image-using-htm-input-type-file
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.readytalk.olive.logic.S3Uploader;
 
 
public class CommonsFileUploadServlet extends HttpServlet {
	private static final String TMP_DIR_PATH = "/temp/";
	private File tmpDir;
	private static final String DESTINATION_DIR_PATH ="/temp/";
	private File destinationDir;
	private static Logger log = Logger.getLogger(CommonsFileUploadServlet.class.getName());
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String realPathTmp = getServletContext().getRealPath(TMP_DIR_PATH);
		tmpDir = new File(realPathTmp);
		//tmpDir = new File(TMP_DIR_PATH);
		log.info(realPathTmp);
		if(!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		String realPathDest = getServletContext().getRealPath(DESTINATION_DIR_PATH);
		destinationDir = new File(realPathDest);
		//destinationDir = new File(DESTINATION_DIR_PATH);
		log.info(realPathDest);
		if(!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH+" is not a directory");
		}
 
	}
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
	    out.println("File uploaded. Please close this window and refresh the editor page.");
	    out.println();
 
		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
		/*
		 *Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above threshold.
		 */
		fileItemFactory.setRepository(tmpDir);
 
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if(item.isFormField()) {
					log.info("File Name = "+item.getFieldName()+", Value = "+item.getString());
				} else {
					//Handle Uploaded files.
					log.info("Field Name = "+item.getFieldName()+
						", File Name = "+item.getName()+
						", Content type = "+item.getContentType()+
						", File Size = "+item.getSize());
					/*
					 * Write file to the ultimate location.
					 */
					File file = new File(destinationDir,item.getName());
					item.write(file);
					S3Uploader.upLoadVideo(file);
					
				}
				out.close();
			}
		}catch(FileUploadException ex) {
			log("Error encountered while parsing the request",ex);
		} catch(Exception ex) {
			log("Error encountered while uploading file",ex);
		}
 
	}
 
}
