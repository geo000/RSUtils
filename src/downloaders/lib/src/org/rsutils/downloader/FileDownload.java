/*
 * Project: Remote Sensing Utilities (Extentions GDAL/OGR)
 * Author:  Igor Garkusha <rsutils.gis@gmail.com>
 *          Ukraine, Dnipropetrovsk
 * 
 * Copyright (C) 2016, Igor Garkusha <rsutils.gis@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
*/

package org.rsutils.downloader;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.cert.*;
import javax.swing.*;
import javax.swing.text.*;
import org.rsutils.*;

public class FileDownload implements Runnable
{
	public FileDownload(URL url, String saveFileName, 
 				JProgressBar prBar, 
				String strInfo, JTextArea txtInfo)
    {
	fileProcessed = false;

        m_url = url;
        m_saveFileName = saveFileName;
        m_prBar = prBar;
        m_textInfo = strInfo;
        m_txtInfo = txtInfo;
        
        new Thread(this).start();
    }
    
    public FileDownload(URL url, String saveFileName, 
			String UserNameAndPasword, 
			JProgressBar prBar, 
			String strInfo, JTextArea txtInfo)
    {
        fileProcessed = false;
	m_UserNameAndPasword = UserNameAndPasword;
        m_url = url;
        m_saveFileName = saveFileName;
        m_prBar = prBar;
        m_textInfo = strInfo;
        m_txtInfo = txtInfo;
        
        new Thread(this).start();
    }
    
    public long getDownloadSize()
    {
	return m_downloadSize;
    }

    @Override
    public void run() 
    {
	Lock();
		
	Utils.printInfo(m_txtInfo, m_textInfo);
			
	RandomAccessFile file = null;
	InputStream stream = null;
	
	URLConnection connection = null;
	
	long downloadSize = 0;
	long size = -1;
	boolean flExists = false;
	long contentLength = -1;
	boolean flHttps = false;
	long fullSize = 0;
        
    if( Utils.fileExists(m_saveFileName) )
	{
		downloadSize = Utils.getFileSize(m_saveFileName);
		flExists = true;
	}
        
        try 
        {
            if(m_UserNameAndPasword.compareTo("") == 0)
			connection = (HttpURLConnection)m_url.openConnection();
		else  // HTTPS Connection (for example: Sentinel-1A, Sentinel-2A)
		{
			flHttps = true;
			connection = (HttpsURLConnection)m_url.openConnection();
			TrustManager[] trustAllCerts = new TrustManager[] { 
				new X509TrustManager() {     
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
						return new X509Certificate[0];
					} 
					public void checkClientTrusted( 
						java.security.cert.X509Certificate[] certs, String authType) {
						} 
					public void checkServerTrusted( 
						java.security.cert.X509Certificate[] certs, String authType) {
					}
				} 
			}; 
			SSLContext sc;
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			((HttpsURLConnection)(connection)).setSSLSocketFactory(sc.getSocketFactory());
			
			String basicAuth = "Basic " + Base64.getEncoder().encodeToString(this.m_UserNameAndPasword.getBytes());
			connection.setRequestProperty("Authorization", basicAuth);
			connection.setDoInput(true);
		}           
            connection.setRequestProperty("Content-Type", "application/octet-stream");
			connection.setRequestProperty("Connection", "close");
			
            connection.setRequestProperty("Range", "bytes=" + downloadSize + "-");
            connection.connect();
                        
		 // connection.getResponseCode()
			
            contentLength = connection.getContentLengthLong();
                        
            if(contentLength >= 1)
            { 
				size = contentLength;
				
				if(downloadSize == 0) fullSize = size;
				else fullSize = downloadSize + size;
				
				if(flExists) Utils.printInfo(m_txtInfo, "Resume downloading. Residual amount: " + Utils.getHumanSize(size) + "\n");
				else Utils.printInfo(m_txtInfo, "Download size: " + Utils.getHumanSize(size) + "\n");
				
				if(flExists) file = new RandomAccessFile(m_saveFileName,"rwd");
				else file = new RandomAccessFile(m_saveFileName,"rw");
				
				file.seek(downloadSize);
				
				stream = connection.getInputStream();    
				
				Utils.printProgress(m_prBar, 0);
										
				long count = 0;
				while(count != size)
				{
					byte buffer [] = null;
					if((size-downloadSize) > MAX_DOWNLOAD_BUFFER_SIZE) 
						buffer = new byte[MAX_DOWNLOAD_BUFFER_SIZE];
					else 
						buffer = new byte[(int)(size-count)];
				
					int read = stream.read(buffer);
					if(read == -1) break;
				
					file.write(buffer, 0, read);
					downloadSize += read;
					count += read;
					Utils.printProgress(m_prBar, (int)( ((float) downloadSize / fullSize)*100 ) );
				}
			}
			else 
			{
				stream = connection.getInputStream();
				Utils.printInfo(m_txtInfo, "SKIP. File already downloaded!\n");
			}
        } 
        catch (Exception e)
        {
			if((flExists)&&(contentLength == -1))
			{
				try { connection.getInputStream().close(); }catch(IOException e2)
				{ 
					try{
						if(flHttps) ((HttpsURLConnection)(connection)).getErrorStream().close();
						else ((HttpURLConnection)(connection)).getErrorStream().close();
					}catch(IOException e3){}
				}
				finally
				{ 
					Utils.printInfo(m_txtInfo, "SKIP. File already downloaded!\n");
				}
			}
			else
				{
					Utils.printInfo(m_txtInfo, "Download ERROR!\n");
					e.printStackTrace();
				}
		}
        finally
        {
            if(file!=null)
            {
                try{ file.close(); }catch(Exception e){}
            }
            
            if(flHttps) ((HttpsURLConnection)(connection)).disconnect();
            else ((HttpURLConnection)(connection)).disconnect();
            connection = null;
            //System.out.println("\nDisconnect...\n");
        }
        
        if(stream != null)
        {
            try{ stream.close(); }catch(Exception e){}
        }
        
        m_downloadSize = downloadSize;
        
        UnLock();
    }
    
    public static void Lock()
    {
		while(fileProcessed) { synchronized(lock) { try { lock.wait(); } catch(InterruptedException e) {} } }
    }
	
    public static void UnLock()
    {
	fileProcessed = true;
        synchronized(lock) 
        {
    		lock.notifyAll();
        }
    }
    
    public static void Wait()
    {
	synchronized(lock) { try { lock.wait(); } catch(InterruptedException e) {} }
    }
    
    // HTTP Download
    public static void doDownload(URL url, String saveFileName, JProgressBar prBar, 
				  String strInfo, JTextArea txtInfo, boolean flTimeOut)
	{
		boolean flContinueDownload = false;
		
		if(flTimeOut)
		{
			Utils.printInfo(txtInfo, "\nTimeout 15 seconds...\n");
			Utils.timeout();
		}
		
		do
		{
			FileDownload download = new FileDownload(url, saveFileName, prBar, strInfo, txtInfo);
			FileDownload.Wait();
			try{ Thread.sleep(5000); }catch(InterruptedException e){}
			if( download.getDownloadSize() < Utils.getFileSize(saveFileName) ) flContinueDownload = true;
			download = null;
		}while(flContinueDownload);
	}
	
	// HTTPS Download
	public static void doDownload(URL url, String saveFileName, String UserNameAndPasword, 
				      JProgressBar prBar, String strInfo, JTextArea txtInfo, boolean flTimeOut)
	{
		boolean flContinueDownload = false;
		
		if(flTimeOut)
		{
			Utils.printInfo(txtInfo, "\nTimeout 15 seconds...\n");
			Utils.timeout();
		}
		
		do
		{
			FileDownload download = new FileDownload(url, saveFileName, UserNameAndPasword, prBar, strInfo, txtInfo);
			FileDownload.Wait();
			try{ Thread.sleep(5000); }catch(InterruptedException e){}
			if( download.getDownloadSize() < Utils.getFileSize(saveFileName) ) flContinueDownload = true;
			download = null;
		}while(flContinueDownload);
				
	}
	    
    private String m_UserNameAndPasword = "";
    private URL m_url;
    private String m_saveFileName;
    private JProgressBar m_prBar = null;
    private JTextArea m_txtInfo = null;
    private String m_textInfo = "";
    private long m_downloadSize = 0;
    private final int MAX_DOWNLOAD_BUFFER_SIZE = 1024;
    private static Object lock = new Object();
    private static boolean fileProcessed = false;
}
