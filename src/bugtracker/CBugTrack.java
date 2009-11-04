/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bugtracker;

/**
 *
 * @author G-SG
 */

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CBugTrack
{

    java.sql.Connection connection = null;
    java.sql.Statement statement;
    public java.sql.ResultSet result_projects = null;
    public java.sql.ResultSet result_tasks = null;
    public java.sql.ResultSet result_bugs = null;
    public java.sql.ResultSet result_profiles = null;
    static String host = null;
    static String port = null;
    static String login = null;
    static String password = null;
    static boolean default_port = true;
    public String data_from_server[][][];
    int projects_count = 0;
    int tasks_count = 0;
    int bugs_count = 0;
    // about access_level //
    // 0 - guest          //
    // 1 - tester         //
    // 2 - developer      //
    // 3 - lead developer //
    // 4 - project manager//

    //////////////////methods///////////////////////
    
    //Connection method
    public int connect_to_server(String login, String password, String host)
    {
        CBugTrack.login = login;
        String database = "ProjectManagement";
        String url = "jdbc:mysql://" + host + "/" + database;
        try
        {
            connection = java.sql.DriverManager.getConnection(url, login, password);
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    int get_all_userlist(String user, String password, int privileges)
    {
        try
        {
            statement = connection.createStatement();
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            statement.executeQuery("SELECT login, password, role FROM User");
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            result_profiles = statement.getResultSet();
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int get_all_project_info()
    {
        try
        {
            statement = null;
            statement = connection.createStatement();
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            statement.executeQuery("SELECT DISTINCT ProjectName, ProjectInfo FROM Project");
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            result_projects = statement.getResultSet();
            projects_count = result_projects.getFetchSize();
            
        } catch (SQLException ex)
        {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement.executeQuery("SELECT ProjectName, TaskName, TaskInfo, Priority, Status, Username FROM Task");
        } catch (SQLException ex) {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result_tasks = statement.getResultSet();
            tasks_count = result_tasks.getFetchSize();
        } catch (SQLException ex) {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement.executeQuery("SELECT ProjectName, BugName, BugInfo, Priority, Status, Username FROM Bug");
        } catch (SQLException ex) {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result_bugs = statement.getResultSet();
            bugs_count = result_bugs.getFetchSize();
        } catch (SQLException ex) {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            while (result_projects.next()) {
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(CBugTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
