package org.converter.jsonxmlconverter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.converter.jsonxmlconverter.converter.JSONXMLConverterI;
import org.converter.jsonxmlconverter.factory.ConverterFactory;

public class ConverterMain
{
	private static Logger logger = Logger.getLogger(ConverterMain.class.getName());
	
	public static void main(String[] args)
	{
		JSONXMLConverterI converter = new ConverterFactory().getConverter();
		
		if(args[0] != null && new File(args[0]).exists())
		{
			if(args[1] != null && new File(args[1].substring(0, args[1].lastIndexOf(File.separator))).exists())
			{
				logger.log(Level.INFO, "JSONXMLConverter starts with below params" 
						+ "\n JSON " + args[0] + "\n XML " + args[1]);
				
				converter.convertJSONtoXML(args[0], args[1]);
				
				logger.log(Level.INFO, "JSONXMLConverter ends");
			}
			else
			{
				logger.log(Level.SEVERE, "xml file path is not valid " + args[1]);
			}
		}
		else
		{
			logger.log(Level.SEVERE, "json file path is not valid" + args[0]);
		}
	}
	
}