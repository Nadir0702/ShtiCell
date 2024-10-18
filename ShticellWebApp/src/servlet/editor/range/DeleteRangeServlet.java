package servlet.editor.range;

import com.google.gson.Gson;
import constants.Constants;
import dto.range.RangeDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Delete Range Servlet", urlPatterns = "/deleteRange")
public class DeleteRangeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        Engine engine = engineManager.getEngine(sheetName);
        String rangeName = request.getParameter(Constants.RANGE_NAME);
        
        if (rangeName == null || rangeName.isEmpty()) {
            throw new IllegalArgumentException("Range name is empty");
        }
        
        try {
            engine.removeRange(rangeName);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
            return;
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
