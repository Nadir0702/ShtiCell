package servlet.editor.range;

import com.google.gson.Gson;
import constants.Constants;
import dto.range.RangeDTO;
import dto.sheet.ColoredSheetDTO;
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

@WebServlet(name = "Add New Range Servlet", urlPatterns = "/addNewRange")
public class AddNewRangeServlet extends HttpServlet {
    
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
        
        try (PrintWriter out = response.getWriter()) {
            Engine engine = engineManager.getEngine(sheetName);
            String rangeName = request.getParameter(Constants.RANGE_NAME);
            String rangeBoundaries = request.getParameter(Constants.RANGE_BOUNDARIES);
            
            if (rangeName == null || rangeName.isEmpty()) {
                throw new IllegalArgumentException("Range name is empty");
            }
            
            if (rangeBoundaries == null || rangeBoundaries.isEmpty()) {
                throw new IllegalArgumentException("Range boundaries are empty");
            }
            
            RangeDTO newRange = engine.addRange(rangeName, rangeBoundaries);
            
            Gson gson = new Gson();
            String json = gson.toJson(newRange);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }
}
