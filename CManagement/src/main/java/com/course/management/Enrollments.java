package com.course.management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Enrollments
 */
@WebServlet("/Enrollments")
public class Enrollments extends HttpServlet {
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
    public Enrollments() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			// Getting parameter
			String course_id = request.getParameter("courseId");
			
			String sql = "SELECT * FROM enrollments WHERE course_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, course_id);
			
			ResultSet rs = ps.executeQuery();
			List<String[]> courseData = new ArrayList<String []>();
			
			while (rs.next()) {
				String arr[] = {rs.getString(1), rs.getString(2), rs.getString(3)};
				courseData.add(arr);
			}
			rs.close();
			con.close();
			
			request.setAttribute("courseId", course_id);
			request.setAttribute("courses", courseData);
			request.getRequestDispatcher("enrollments.jsp").forward(request, response);
			
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
