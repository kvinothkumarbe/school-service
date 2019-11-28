/**
 * 
 */
package com.cts.ms.school.util;

import org.springframework.stereotype.Component;

/**
 * @author 430040
 *
 */
@Component
public class XMLutils {
	
	static String html="<html><head><style>table {font-family: arial, sans-serif;border-collapse: collapse; width: 100%;}td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}tr:nth-child(even) {background-color: #dddddd;}</style></head><body><h2>Student Details</h2> ::replaceData:: </body></html>";
	
	public static String gnerateXML(String input){
		System.out.println("Input XML: "+input);
		input=input.replaceAll("<List>", "<table><tr><th>Name</th><th>Class</th></tr>").replaceAll("</List>", "</table>").replaceAll("item>", "tr>").replaceAll("name>", "td>").replaceAll("className>", "td>");
		System.out.println("Converted Input XML: "+input);
		String result=html.replace("::replaceData::", input);
		System.out.println("Output XML: "+result);
		return result;
	}

}
