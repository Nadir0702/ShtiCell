package servlet;

import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import manager.EngineManager;
import logic.engine.Engine;
import logic.engine.EngineImpl;
import manager.UserManager;
import user.User;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Load Sheet Servlet", urlPatterns = "/loadSheetFromFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetFromFileServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Part filePart = request.getPart("file");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        
        // -------- currently here because there is no login page yet -----------------------
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
        synchronized (this) {
            if (!userManager.isUserExists(usernameFromParameter)){
                userManager.addUser(usernameFromParameter, new User(usernameFromParameter));
            }
        }
        // ----------------------------------------------------------------------------------
        
        String username = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, username)) {
            return;
        }
        
        User user = userManager.getUser(username);
        
        try{
            Engine engine = new EngineImpl(user);
            engine.loadDataFromStream(filePart.getInputStream());
            String sheetName = engine.getName();
            
            synchronized (this) {
                if (engineManager.isEngineExists(sheetName)) {
                    throw new IllegalArgumentException("Sheet with name [" + sheetName + "] already exists");
                } else {
                    engineManager.addEngine(sheetName, engine);
                }
            }
            
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(engine.getSheetMetaData(username)));
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (RuntimeException e) {
            response.getWriter().println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}