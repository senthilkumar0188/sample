package org.converter.jsonxmlconverter.factory;

import org.converter.jsonxmlconverter.converter.JSONXMLConverterI;
import org.converter.jsonxmlconverter.converter.JSONXMLConverterImpl;

public class ConverterFactory
{
	public JSONXMLConverterI getConverter()
	{
		return new JSONXMLConverterImpl();
	}
	
}