package com.course.management;

import java.util.*;

import java.io.IOException;
// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/coursesdb3";
	
	// Database Credentials
	static final String USER = "root";
	static final String PASSWORD = "password";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Getting user's information from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			// Fetching course informations
			String sql = "SELECT * FROM courses";
			PreparedStatement ps = con.prepareStatement(sql);
			
			// Storing course information
			List<String[]> courseData = new ArrayList<String[]>();
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String course_id = rs.getString(1);
				String course_name = rs.getString(2);
				String course_teacher_user_name = rs.getString(3);
				
				// Getting teacher's full name
				String sql2 = "SELECT full_name FROM users WHERE user_name = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, course_teacher_user_name);
				
				ResultSet rs2 = ps2.executeQuery();
				String course_teacher_full_name = course_teacher_user_name;
				
				if (rs2.next()) {
					course_teacher_full_name = rs2.getString(1);
				}
				rs2.close();
				
				String arr[] = {course_id, course_name, course_teacher_full_name};
				courseData.add(arr);
			}
			rs.close();
			con.close();
			
			// passing data to home-admin.jsp page
			request.setAttribute("user_name", user_name);
			request.setAttribute("full_name", full_name);
			request.setAttribute("courses", courseData);
			request.getRequestDispatcher("home-admin.jsp").forward(request, response);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
