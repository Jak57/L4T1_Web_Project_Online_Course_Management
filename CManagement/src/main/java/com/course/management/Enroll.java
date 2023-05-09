package com.course.management;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class Enroll
 */
@WebServlet("/Enroll")
public class Enroll extends HttpServlet {
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
    public Enroll() {
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
			
			String sql3 = "SELECT * FROM courses WHERE course_id NOT IN(SELECT course_id FROM enroll WHERE user_name= ?)";
			PreparedStatement ps3 = con.prepareStatement(sql3);
			ps3.setString(1, user_name);
			
			ResultSet rs3 = ps3.executeQuery();
			List<String[]> courseData = new ArrayList<String[]>();
			
			while (rs3.next()) {
				String course_id = rs3.getString(1);
				String course_name = rs3.getString(2);
				String course_teacher_user_name = rs3.getString(3);
				
				// Getting teacher's full name
				String sql2 = "SELECT full_name FROM users WHERE user_name = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, course_teacher_user_name);
		
				ResultSet rs2 = ps2.executeQuery();
				String course_teacher_full_name = "";
				
				if (rs2.next()) {
					course_teacher_full_name = rs2.getString(1);
				}
				rs2.close();
				
				String arr[] = {course_id, course_name, course_teacher_full_name};
				courseData.add(arr);
			}
			rs3.close();
			con.close();
			
			request.setAttribute("fullname", full_name);
			request.setAttribute("userid", user_id);
			request.setAttribute("username", user_name);
			request.setAttribute("courses", courseData);
			
			request.getRequestDispatcher("enroll.jsp").forward(request, response);
			
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
