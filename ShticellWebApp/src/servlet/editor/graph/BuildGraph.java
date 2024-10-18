package servlet.editor.graph;

import com.google.gson.Gson;
import constants.Constants;
import dto.returnable.EffectiveValueDTO;
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
import java.util.LinkedHashMap;

@WebServlet(name = "Build Graph Servlet", urlPatterns = "/buildGraph")
public class BuildGraph extends HttpServlet {
    
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
        
        String rangeToBuildGraphFrom = request.getParameter(Constants.RANGE_NAME);
        if (rangeToBuildGraphFrom == null || rangeToBuildGraphFrom.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("No range boundaries were given");
            response.getWriter().flush();
            return;
        }
        
        Engine engine = engineManager.getEngine(sheetName);
        Gson gson = new Gson();
        
        try (PrintWriter out = response.getWriter()) {
            try {
                LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graph =
                        engine.getGraphFromRange(rangeToBuildGraphFrom);
                String json = gson.toJson(graph);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }  catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
                response.getWriter().flush();
            }
        }
    }
}
