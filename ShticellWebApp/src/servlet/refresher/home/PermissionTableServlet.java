package servlet.refresher.home;

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
import java.util.List;

@WebServlet(name = "Permission Table Servlet", urlPatterns = "/refreshPermissionTable")
public class PermissionTableServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = engineManager.getEngine(request.getParameter(Constants.SHEET_NAME));
            if (engine == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("No such sheet");
                return;
            }
            
            List<PermissionDTO> permissionDTOList = engine.getAllPermissions();
            
            
            
            String json = gson.toJson(permissionDTOList);
            out.println(json);
            out.flush();
        }
    }
}