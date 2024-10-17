package servlet.editor.sheet;

import com.google.gson.Gson;
import constants.Constants;
import dto.cell.CellDTO;
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

@WebServlet(name = "Get Single Cell Data Servlet", urlPatterns = "/getSingleCellData")
public class GetSingleCellDataServlet extends HttpServlet {
    
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
            String cellID = request.getParameter(Constants.CELL_ID);
            
            if (cellID == null || cellID.isEmpty()) {
                throw new IllegalArgumentException("Got No cell ID");
            }
            
            CellDTO cellData = engine.getSingleCellData(cellID);
            Gson gson = new Gson();
            String json = gson.toJson(cellData);
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
