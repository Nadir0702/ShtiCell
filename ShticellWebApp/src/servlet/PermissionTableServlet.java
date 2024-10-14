package servlet;

import com.google.gson.Gson;
import constants.Constants;
import dto.permission.PermissionDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "Permission Table Servlet", urlPatterns = "/refreshPermissionTable")
public class PermissionTableServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = engineManager.getEngine(request.getParameter(Constants.SHEETNAME));
            if (engine == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("No such sheet");
            }
            
            Set<PermissionDTO> permissionDTOSet = engine.getAllPermissions();
            
            
            
            String json = gson.toJson(permissionDTOSet);
            out.println(json);
            out.flush();
        }
    }
}
