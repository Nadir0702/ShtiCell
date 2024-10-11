package servlet;

import com.google.gson.Gson;
import dto.SheetMetaDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.EngineManager;
import logic.engine.Engine;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "Sheet Table Servlet", urlPatterns = "/refreshSheetTable")
public class SheetTableServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Map<String, Engine> enginesList = engineManager.getEngines();
            Set<SheetMetaDataDTO> sheetMetaDataDTOSet = new LinkedHashSet<>();
            
            enginesList.forEach((name, engine) ->
                    sheetMetaDataDTOSet.add(engine.getSheetMetaData(SessionUtils.getUsername(request)))
            );
            
            String json = gson.toJson(sheetMetaDataDTOSet);
            out.println(json);
            out.flush();
        }
    }
}
