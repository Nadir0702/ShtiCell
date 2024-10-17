package servlet.editor.version;

import com.google.gson.Gson;
import constants.Constants;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetAndCellDTO;
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

@WebServlet(name = "Load Sheet Version Servlet", urlPatterns = "/loadSheetVersion")
public class LoadSheetVersionServlet extends HttpServlet {
    
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
        
        String version = request.getParameter(Constants.VERSION);
        try (PrintWriter out = response.getWriter()) {
            Engine engine = engineManager.getEngine(sheetName);
            
            if (version == null || version.isEmpty()) {
                throw new IllegalArgumentException("Invalid version");
            }
            
            ColoredSheetDTO coloredSheet = engine.getSheetVersionAsDTO(Integer.parseInt(version));
            
            Gson gson = new Gson();
            String json = gson.toJson(coloredSheet);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Given version [" + version + "] is not a valid version number");
            response.getWriter().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }
}
