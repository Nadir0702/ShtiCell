package servlet.permission;

import com.google.gson.Gson;
import constants.Constants;
import dto.permission.ReceivedPermissionRequestDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import manager.UserManager;
import user.User;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Answer Permission Request Servlet", urlPatterns = "/answerPermissionRequest")
public class AnswerPermissionRequestServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, username)) {
            return;
        }
        
        String answerAsString = request.getParameter(Constants.REQUEST_ANSWER);
        if (answerAsString == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("No answer given");
            response.getWriter().flush();
            return;
        }
        
        try {
            boolean answer = this.getAnswerFromParameter(answerAsString);
            
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            
            ReceivedPermissionRequestDTO requestToReplyTo = gson.fromJson(reader, ReceivedPermissionRequestDTO.class);
            Engine engine = engineManager.getEngine(requestToReplyTo.getSheetName());
            User sender = userManager.getUser(requestToReplyTo.getSender());
            User receiver = userManager.getUser(username);
            
            this.handleReplyInUser(requestToReplyTo, sender, receiver);
            this.handleReplyInEngine(requestToReplyTo, engine, answer);
            
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }
    
    private void handleReplyInEngine(ReceivedPermissionRequestDTO requestToReplyTo, Engine engine, boolean answer) {
        if (engine == null) {
            throw new IllegalArgumentException("No Sheet found for name " + requestToReplyTo.getSheetName());
        } else {
            engine.updatePermissionForUser(requestToReplyTo.getSender(), answer, requestToReplyTo.getRequestID());
        }
    }
    
    private void handleReplyInUser(ReceivedPermissionRequestDTO requestToReplyTo, User sender, User receiver) {
        if (sender == null) {
            throw new IllegalArgumentException("No request from user " + requestToReplyTo.getSender() + " found");
        } else {
            receiver.removePermissionRequest(requestToReplyTo.getSheetName(), requestToReplyTo.getRequestID());
        }
    }
    
    private boolean getAnswerFromParameter(String answerAsString) {
        if (answerAsString.equalsIgnoreCase("false")) {
            return false;
        } else if (answerAsString.equalsIgnoreCase("true")) {
            return true;
        } else {
            throw new IllegalArgumentException("Wrong answer given");
        }
    }
}
