/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bugtracker;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author yusanenko
 */

public class CXML {

    public static String[][] answer;
    public static String[] answer_profile = new String[3];

    public void write_data_to_xml(java.sql.ResultSet result_projects, java.sql.ResultSet result_tasks, java.sql.ResultSet result_bugs)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        DOMImplementation dom_implementation = builder.getDOMImplementation();
        Document xml_file = dom_implementation.createDocument(null, null, null);
        Element e0 = xml_file.createElement("Projects");
        xml_file.appendChild(e0);
        try {
            while (!result_projects.next()) {
                Element e1 = xml_file.createElement("Project");
                e0.appendChild(e1);
                e1.setAttribute("name", result_projects.getString(0));
                Element e2 = xml_file.createElement("Project_info");
                e1.appendChild(e2);
                e2.appendChild(xml_file.createTextNode(result_projects.getString(1)));

                Element e3 = xml_file.createElement("Tasks");
                e1.appendChild(e3);
                while (!result_tasks.next()) {
                   if (result_tasks.getString(0).equals(result_projects.getString(0))) {
                   Element e4 = xml_file.createElement("Task");
                   e3.appendChild(e4);
                   e4.setAttribute("name", result_tasks.getString(1));
                   //e4.appendChild(xml_file.createTextNode(bugtracker.CBugTrack.result_tasks.getString(2)));
                   e4.setAttribute("priority", result_tasks.getString(3));
                   e4.setAttribute("status", result_tasks.getString(4));
                   e4.setAttribute("assigned", result_tasks.getString(5));
                   Element e5 = xml_file.createElement("Task_info");
                   e4.appendChild(e5);
                   e5.appendChild(xml_file.createTextNode(result_tasks.getString(2)));
                   }
                }
                   Element e6 = xml_file.createElement("Bugs");
                e1.appendChild(e6);
                while (!result_bugs.next()) {
                    if (result_bugs.getString(0).equals(result_projects.getString(0))) {
                    Element e7 = xml_file.createElement("Bug");
                    e6.appendChild(e7);
                    e7.setAttribute("name", result_bugs.getString(1));
                    //e7.appendChild(xml_file.createTextNode(bugtracker.CBugTrack.result_bugs.getString(2)));
                    e7.setAttribute("priority", result_bugs.getString(3));
                    e7.setAttribute("status", result_bugs.getString(4));
                    e7.setAttribute("assigned", result_bugs.getString(5));
                    Element e8 = xml_file.createElement("Bug_info");
                    e7.appendChild(e8);
                    e8.appendChild(xml_file.createTextNode(result_bugs.getString(2)));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        DOMSource domSource = new DOMSource(xml_file);
        TransformerFactory transformer_factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformer_factory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult stream_result = new StreamResult("/Documents/cache.xml");
        try {
            transformer.transform(domSource, stream_result);
        } catch (TransformerException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

    public void read_data_from_xml(String proj_name, String request)
    {
      //TODO ans = new String[20][2];
      try {
      File file = new File("cache.xml");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document xml_file = builder.parse(file);
      xml_file.getDocumentElement().normalize();
      NodeList nodeLst = xml_file.getElementsByTagName("Project");
      int f = 0;
      for (int i = 0; i < nodeLst.getLength(); i++)
      {
        Node Project = nodeLst.item(i);
        Element Proj = (Element) nodeLst.item(i);
        String name = Proj.getAttribute("name");
        NodeList Projs_info = Proj.getElementsByTagName("Project_info");
        Element Proj_info = (Element) Projs_info.item(0);
        NodeList LstInfo = Proj_info.getChildNodes();
        String info = LstInfo.item(0).getNodeValue();

        if ((proj_name.equals(name)) || (proj_name.equals("All")))
         if (request.equals("Tasks"))
         {
          Element Task = (Element) Project;
          NodeList Tasks = Task.getElementsByTagName("Task");
           for (int j = 0; j < Tasks.getLength(); j++)
            {
             Element current_Task = (Element)Tasks.item(j);
             String status = current_Task.getAttribute("status");
             answer[f][0] = status;
             NodeList Tasks_info = current_Task.getElementsByTagName("Task_info");
             Element Task_info = (Element) Tasks_info.item(0);
             NodeList Info_lst = Task_info.getChildNodes();
             String T_info=Info_lst.item(0).getNodeValue();
             answer[f][1] = T_info;
             f++;
            }
         }
        if ((proj_name.equals(name)) || (proj_name.equals("All")))
         if (request.equals("Bugs"))
         {
          Element Bug = (Element) Project;
          NodeList Bugs = Bug.getElementsByTagName("Bug");
           for (int l = 0; l < Bugs.getLength(); l++)
            {
             Element current_Bug = (Element)Bugs.item(l);
             String status = current_Bug.getAttribute("status");
             answer[f][0] = status;
             NodeList Bugs_info = current_Bug.getElementsByTagName("Bug_info");
             Element Bug_info = (Element) Bugs_info.item(0);
             NodeList Info_lst = Bug_info.getChildNodes();
             String B_info = Info_lst.item(0).getNodeValue();
             answer[f][1] = B_info;
             f++;
            }
         }
      }
      }
      catch (Exception e) {}
      }

public void write_profile_to_xml(String user_name, String password, String port, String host)
 {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = null;
   try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) { }

  DOMImplementation dom_implementation = builder.getDOMImplementation();
  Document xml_profile = dom_implementation.createDocument(null, null, null);
  Element el = xml_profile.createElement("Profile");
  xml_profile.appendChild(el);
  Element el2 = xml_profile.createElement("User");
  el.appendChild(el2);
  el2.setAttribute("name", user_name);
  el2.setAttribute("password", password);
  el2.setAttribute("port", port);
  el2.setAttribute("host", host);

  DOMSource domSource = new DOMSource(xml_profile);
  TransformerFactory trans_factory = TransformerFactory.newInstance();
  Transformer trans = null;
  try {
      trans = trans_factory.newTransformer();
      }catch (TransformerConfigurationException ex) {}
  trans.setOutputProperty(OutputKeys.METHOD, "xml");
  trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
  trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
  trans.setOutputProperty(OutputKeys.INDENT, "yes");
  StreamResult stream_res = new StreamResult("user.xml");
  try {
       trans.transform(domSource, stream_res);
      } catch (TransformerException ex) { }
 }

public void read_profile_from_xml()
 {
      File file = new File("user.xml");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
      Document xml_profile = null;
        try {
            xml_profile = builder.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CXML.class.getName()).log(Level.SEVERE, null, ex);
        }
      xml_profile.getDocumentElement().normalize();
      NodeList Userlist = xml_profile.getElementsByTagName("User");
        Element Project = (Element) Userlist.item(0);
        String login = Project.getAttribute("name");
        answer_profile[0] = login;
        String password = Project.getAttribute("password");
        answer_profile[1] = password;
        String host = Project.getAttribute("host");
        answer_profile[2] = host;
 }
}
