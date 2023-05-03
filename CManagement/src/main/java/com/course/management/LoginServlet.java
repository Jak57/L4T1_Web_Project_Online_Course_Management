package com.course.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		
		try {
			PrintWriter out = response.getWriter();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursesdb3", "root", "password");
			
			String name = request.getParameter("name");
			String pwd = request.getParameter("pwd");
			
			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_name = ? AND pass_word = ?");
			
			ps.setString(1, name);
			ps.setString(2, pwd);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				
				// Getting user's informations
				String full_name = rs.getString("full_name");
				String utype = rs.getString("utype");
				String user_name = rs.getString("user_name");
				String user_id = rs.getString("user_id");
				
				System.out.println("Full name: " + full_name);
				System.out.println("User type: " + utype);
				System.out.println("User name: " + user_name);
				System.out.println("User id: " + user_id);
				
				// Storing user credentials into session
				HttpSession session = request.getSession(true);
				session.setAttribute("full_name", full_name);
				session.setAttribute("user_name", user_name);
				session.setAttribute("user_id", user_id);
				
				// Rendering pages based on user type
				switch(rs.getString("utype")) {
					case "admin": {
						response.sendRedirect("Admin");
						break;
					}
					case "teacher": {
						// out.println("Welcome Teacher");
						response.sendRedirect("Teacher");
						break;
					}
					case "student": {
						// out.println("Welcome Student");
						response.sendRedirect("Student");
						break;
					}
				}
				
				// RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp");
				// rd.forward(request, response);
			} else {
				out.println("Login failed!");
				out.println("Try again!!");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
