package com.gnguyen.web.clickcounter.servlet;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gnguyen.web.clickcounter.dao.ClickDao;

/**
 * Servlet implementation class ClickCounter
 */
@WebServlet("/ClickCounter")
public class ClickCounter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	int count;
	private ClickDao dao;
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Sets a cookie for the user, so the counter does not increate
		// every time the user presses refresh
		HttpSession session = request.getSession(true);
		
		// Set the session valid for 3 seconds
		// count is only incremented if user stays on page for at least 3 seconds
		session.setMaxInactiveInterval(3);
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		
		if (session.isNew()) {
			count++;
		}
		writer.println("Page visits: " + count);
	}

	@Override
	public void init() throws ServletException {
		dao = new ClickDao();
		
		try {
			count = dao.getCount();
		} catch (Exception e){
			getServletContext().log("An exception ocurrred in ClickCounter", e);
			throw new ServletException("An exception ocurred in ClickCounter" + e.getMessage());
		}
	}
	
	public void destroy() {
		super.destroy();
		try {
			dao.save(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}





