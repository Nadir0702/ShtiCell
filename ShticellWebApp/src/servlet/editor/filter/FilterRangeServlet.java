package servlet.editor.filter;

import com.google.gson.Gson;
import constants.Constants;
import dto.filter.FilterParametersDTO;
import dto.sheet.ColoredSheetDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Filter Range Servlet", urlPatterns = "/filterRange")
public class FilterRangeServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        Engine engine = engineManager.getEngine(sheetName);
        BufferedReader reader = request.getReader();
        ColoredSheetDTO filteredSheet;
        Gson gson = new Gson();
        
        try (PrintWriter out = response.getWriter()) {
            try {
                FilterParametersDTO filterParameters = gson.fromJson(reader, FilterParametersDTO.class);
                filteredSheet = engine.filterRangeOfCells(filterParameters);
                String json = gson.toJson(filteredSheet);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ClassCastException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Cannot sort by non-numeric column");
                response.getWriter().flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
                response.getWriter().flush();
            }
        }
    }
}
