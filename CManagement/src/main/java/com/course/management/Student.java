package com.course.management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*; // for using ArrayList

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Student
 */
@WebServlet("/Student")
public class Student extends HttpServlet {
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
    public Student() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Getting user's credentials from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");
		String user_id = (String)request.getSession(false).getAttribute("user_id");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			// Fetching course informations for student
			String sql = "SELECT * FROM enroll WHERE user_name = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_name);
			
			ResultSet rs = ps.executeQuery();
			// Storing course informations
			List<String[]> courseData = new ArrayList<String[]>();
			
			while (rs.next()) {
				String course_id = rs.getString(1);
				String course_name = rs.getString(2);
				String course_teacher = rs.getString(3);
				
				String arr[] = {course_id, course_name, course_teacher};
				courseData.add(arr);
			}
			rs.close();
			con.close();
			
			// Passing data to home-student.jsp page
			request.setAttribute("userid", user_id);
			request.setAttribute("username", user_name);
			request.setAttribute("fullname", full_name);
			request.setAttribute("courses", courseData);
			
			request.getRequestDispatcher("home-student.jsp").forward(request, response);
			
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
