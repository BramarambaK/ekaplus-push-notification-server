package com.eka.notification.push.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

/**
 * The Class ManifestService.
 */
@Service
public class ManifestService {

	
	/**
	 * Gets the manifest attributes.
	 *
	 * @return the manifest attributes
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Attributes getManifestAttributes(){
		 Manifest mf=null;
		 InputStream resourceAsStream=null;
		 Attributes atts=null;
	    try {
	    	 resourceAsStream = SpringBootApplication.class.getResourceAsStream("/META-INF/MANIFEST.MF");
	 	     mf = new Manifest();
			 mf.read(resourceAsStream);
			 atts = mf.getMainAttributes();
		}catch (FileNotFoundException fno){
			fno.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	    return atts;	    		
	}


}