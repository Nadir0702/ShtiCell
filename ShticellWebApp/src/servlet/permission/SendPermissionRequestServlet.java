package servlet.permission;

import com.google.gson.Gson;
import dto.permission.SentPermissionRequestDTO;
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

@WebServlet(name = "Send Permission Request Servlet", urlPatterns = "/sendPermissionRequest")
public class SendPermissionRequestServlet  extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, username)) {
            return;
        }
        
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        
        SentPermissionRequestDTO requestToSend = gson.fromJson(reader, SentPermissionRequestDTO.class);
        Engine engine = engineManager.getEngines().get(requestToSend.getRequestedEngineName());
        if (engine == null) {
            response.getWriter().println("No Sheet found for name " + requestToSend.getRequestedEngineName());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            try {
                engine.createNewPermissionRequest(requestToSend, username);
            } catch (RuntimeException e) {
                response.getWriter().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
