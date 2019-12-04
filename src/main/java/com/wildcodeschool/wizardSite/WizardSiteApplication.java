package com.wildcodeschool.wizardSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.sql.*;



@Controller
@SpringBootApplication
public class WizardSiteApplication {

static Connection con;  // Eine Verbindung zur Datenbank aufrufen

	public static void main(String[] args) throws Exception {
	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wild_db_quest","root","Skyanna123!");	
	SpringApplication.run(WizardSiteApplication.class, args);
	}
	
	@RequestMapping("/")
    @ResponseBody
    public String index() throws Exception {
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery("Select * from team");
		String output="<h1>List of teams</h1><ul>";
		while(rs.next()){
			output += "<li><a href=team/"+rs.getInt(1)+">"+rs.getString(2)+"</a></li>";
		} 
		output += "</ul>";
		return output;
	   
    }
	
	@RequestMapping("/team/{team_id}")
    @ResponseBody
    public String hello(@PathVariable int team_id) throws Exception {
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery("select * from team where id = "+ team_id);
		//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wild_db_quest","root","root");  / werden mehere verbindungen zum  Server aufzurufen
		if(rs.next()){
			String output = "<h1>"+ rs.getString("name") + "</h1><ul>";
			Statement stmt2=con.createStatement();
			ResultSet rs2=stmt2.executeQuery("select firstname, lastname from wizard join player on wizard.id = player.wizard_id where player.team_id="+ team_id);
			while(rs2.next()){
				output += "<li>" +rs2.getString("firstname") + " " + rs2.getString("lastname") + "</li>";
			}
			output += "</ul>";
			return output;
		}
		else {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such team!");
		}
	}
  
	/*
	@RequestMapping("/hello/{name}")
    @ResponseBody
    public String hello(@PathVariable String name) throws Exception {
		
	//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wild_db_quest","root","root");  / werden mehere verbindungen zum  Server aufzurufen
        return "Hello" + name;
    }
	*/
}
