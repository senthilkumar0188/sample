## JSONXMLConverter

Download the below jars and place it in lib folder

	1) jackson-annotations-2.10.0
	2) jackson-core-2.10.0
	3) jackson-databind-2.10.0

To build the jar open up command prompt and use below command with updated build file path.

	ant -buildfile "C:\Users\build.xml"

To run the jar open up command prompt and use below command with updated jar, input json and output xml file paths.

	java -jar "C:\Users\JSONXMLConverter.jar" "C:\Users\example.json" "C:\Users\example.xml"