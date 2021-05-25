package org.converter.jsonxmlconverter.converter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONXMLConverterImpl implements JSONXMLConverterI
{
	private Logger logger = Logger.getLogger(JSONXMLConverterImpl.class.getName());
	static final String XML_ATTR_NAME = "name";
	static final String XML_ELEMENT_OBJECT = "object";
	static final String XML_ELEMENT_ARRAY = "array";
	static final String XML_ELEMENT_STRING = "string";
	static final String XML_ELEMENT_NUMBER = "number";
	static final String XML_ELEMENT_BOOLEAN = "boolean";
	static final String XML_ELEMENT_NULL = "null";

	
	private Element createJsonObjElement(Entry<?, ?> jsonObj, Document document, Element parentElement, String strElement)
	{
		Element childElement = document.createElement(strElement);
		
		if(jsonObj.getKey() != null)
		{
			Attr attr = document.createAttribute(XML_ATTR_NAME);
			attr.setValue(jsonObj.getKey().toString());
			
			childElement.setAttributeNode(attr);
		}
		
		if(jsonObj.getValue() != null && (!strElement.equals(XML_ELEMENT_OBJECT) && !strElement.equals(XML_ELEMENT_ARRAY)))
		{
			childElement.appendChild(document.createTextNode(jsonObj.getValue().toString()));
		}
		
		parentElement.appendChild(childElement);
		
		return childElement;
	}
	
	
	private void processJsonObj(Map<?, ?> jsonMap, Document document, Element parentElement)
	{
		for(Entry<?, ?> jsonObj : jsonMap.entrySet())
		{	
			if(jsonObj.getValue() instanceof Map)
			{	
				processJsonObj((Map<?, ?>) jsonObj.getValue(), document, createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_OBJECT));
			}
			else if(jsonObj.getValue() instanceof List)
			{
				processJsonAry((List<?>) jsonObj.getValue(), document, createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_ARRAY));
			}
			else if(jsonObj.getValue() instanceof String)
			{
				createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_STRING);
			} 
			else if(jsonObj.getValue() instanceof Number)
			{
				createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_NUMBER);
			} 
			else if(jsonObj.getValue() instanceof Boolean) 
			{
				createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_BOOLEAN);
			} 
			else 
			{
				createJsonObjElement(jsonObj, document, parentElement, XML_ELEMENT_NULL);
			}
		}
	}
	

	private Element createJsonAryElement(Object jsonObj, Document document, Element parentElement, String strElement)
	{
		Element childElement = document.createElement(strElement);
		
		parentElement.appendChild(childElement);
		
		if(jsonObj != null && !strElement.equals(XML_ELEMENT_OBJECT) && !strElement.equals(XML_ELEMENT_ARRAY))
		{
			childElement.appendChild(document.createTextNode(jsonObj.toString()));
		}
		
		return childElement;
	}
	
	
	private void processJsonAry(List<?> jsonAry, Document document, Element parentElement)
	{
		for(Object jsonObj : jsonAry)
		{
			if(jsonObj instanceof Map)
			{
				processJsonObj((Map<?, ?>) jsonObj, document, createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_OBJECT));
			}
			else if(jsonObj instanceof List)
			{
				processJsonAry((List<?>) jsonObj, document, createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_ARRAY));
			} 
			else if(jsonObj instanceof String)
			{
				createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_STRING);
			} 
			else if(jsonObj instanceof Number) 
			{
				createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_NUMBER);
			} 
			else if(jsonObj instanceof Boolean) 
			{
				createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_BOOLEAN);
			} 
			else 
			{
				createJsonAryElement(jsonObj, document, parentElement, XML_ELEMENT_NULL);
			}
		}
	}
	
	
	private void writeXmlFile(Document document, String xml_file)
	{
		logger.log(Level.INFO, "writeXmlFile starts");
		
		try
		{
			StreamResult streamResult = new StreamResult(new File(xml_file));
	        
	        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), streamResult);
		}
		catch(Exception exp)
		{
			logger.log(Level.SEVERE, "writeXmlFile" + exp.getMessage());
		}
		logger.log(Level.INFO, "writeXmlFile ends");
	}
	
	
	private void readJsonFile(String json_file, Document document)
	{
		logger.log(Level.INFO, "readJsonFile starts");

		try
		{
			Map<?, ?> jsonMap = new ObjectMapper().readValue(new File(json_file), Map.class);
			
		    if(jsonMap != null && jsonMap.size() > 0)
		    {
		    	Element root = document.createElement(XML_ELEMENT_OBJECT);
		         
		 	    document.appendChild(root);
		         
		 		processJsonObj(jsonMap, document, root);
		    }
		    else
		    {
		    	throw new Exception("Invalid json " + json_file);
		    }
		}
		catch(Exception exp)
		{
			logger.log(Level.SEVERE, "readJsonFile" + exp.getMessage());
		}
		logger.log(Level.INFO, "readJsonFile ends");
	}
	
	
	public void convertJSONtoXML(String json_file, String xml_file)
	{
		try 
		{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	        
			readJsonFile(json_file, document);
			
			writeXmlFile(document, xml_file);	
		}
		catch(Exception exp)
		{
			logger.log(Level.SEVERE, "JSONXMLConverter" + exp.getMessage());
		}
	}
	
}