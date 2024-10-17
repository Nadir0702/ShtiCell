package servlet.editor;

import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.EngineManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Set Engine To Session Servlet", urlPatterns = "/setEngineToSession")
public class SetEngineToSessionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, username)) {
            return;
        }
        
        String engineName = request.getParameter(Constants.SHEET_NAME);
        if (engineName == null || engineName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (!engineManager.isEngineExists(engineName)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("No such engine with name " + engineName);
            response.getWriter().flush();
        } else {
            request.getSession(false).setAttribute(Constants.SHEET_NAME, engineName);
        }
    }
}
