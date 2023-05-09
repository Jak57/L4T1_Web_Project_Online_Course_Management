package com.course.management;

import java.io.IOException;
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
 * Servlet implementation class EnrollCourse
 */
@WebServlet("/EnrollCourse")
public class EnrollCourse extends HttpServlet {
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
    public EnrollCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Getting user's credentials from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");
		String user_id = (String)request.getSession(false).getAttribute("user_id");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			// Getting enrolled course's informations
			String course_id = request.getParameter("courseId");
			String course_name = request.getParameter("courseName");
			String course_teacher = request.getParameter("courseTeacher");
			
			String sql = "INSERT INTO enroll VALUES(?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, course_id);
			ps.setString(2, course_name);
			ps.setString(3, course_teacher);
			ps.setString(4, user_name);
			
			ps.executeUpdate();
			String sql1 = "INSERT INTO enrollments VALUES(?, ?, ?)";
			PreparedStatement ps1 = con.prepareStatement(sql1);
			
			ps1.setString(1, user_id);
			ps1.setString(2, full_name);
			ps1.setString(3, course_id);
			ps1.executeUpdate();

			ps.close();
			ps1.close();
			con.close();
			response.sendRedirect("Student");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
