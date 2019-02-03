/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secure;

import com.google.gson.Gson;
import entity.Member;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import secure.entity.Role;
import secure.entity.UserRoles;
import session.MemberFacade;
import session.RoleFacade;
import session.UserFacade;
import session.UserRolesFacade;
import util.EncriptPass;
import util.PageReturner;

/**
 *
 * @author Melnikov
 */
@WebServlet(loadOnStartup = 1,name = "Secure", urlPatterns = {
    "/restlogin",
    "/login",
    "/logout",
    "/showLogin",
    "/showUserRoles",
    "/changeUserRole"
})
public class Secure extends HttpServlet {
   
    @EJB RoleFacade roleFacade;
    @EJB UserFacade userFacade;
    @EJB MemberFacade memberFacade;
    @EJB UserRolesFacade userRolesFacade;

    @Override
    public void init() throws ServletException {
        List<Member> listMembers = memberFacade.findAll();
        if(listMembers.isEmpty()){
            EncriptPass ep = new EncriptPass();
            String salts = ep.createSalts();
            String encriptPass = ep.setEncriptPass("admin", salts);
            User user = new User("Сидор", "Сидоров", "454545454", "К-Ярве");
            userFacade.create(user);
            Member member = new Member(user, "admin", encriptPass, salts);
            memberFacade.create(member);
            Role role = new Role();
            role.setName("ADMIN");
            roleFacade.create(role);
            UserRoles ur = new UserRoles();
            ur.setMember(member);
            ur.setRole(role);
            userRolesFacade.create(ur);
            role.setName("USER");
            roleFacade.create(role);
            ur.setMember(member);
            ur.setRole(role);
            userRolesFacade.create(ur);
        }
    }
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF8");
        HttpSession session = request.getSession(false);
        Member member = null;
        if(session != null){
            try {
                member = (Member) session.getAttribute("regUser");
            } catch (Exception e) {
                member = null;
            }
        }
            
        SecureLogic sl = new SecureLogic();
        String path = request.getServletPath();
        if(null != path)
            switch (path) {
        case "/restlogin":
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            if(login == null || login.isEmpty()){
                try(PrintWriter pw = new PrintWriter(response.getWriter())){
                    pw.write(new Gson().toJson("false"));
                }
                break;
            }
            member = memberFacade.memberByLogin(login);
            if(member != null){
                EncriptPass ep = new EncriptPass();
                String salts = member.getSalts();
                String encriptPass = ep.setEncriptPass(password, salts);
                if(encriptPass.equals(member.getPassword())){
                    session = request.getSession(true);
                    session.setAttribute("regUser", member);
                    try(PrintWriter pw = new PrintWriter(response.getWriter())){
                        pw.write(new Gson().toJson(member.getUser()));
                    }
                }
                break;
            }else{
                try(PrintWriter pw = new PrintWriter(response.getWriter())){
                    pw.write(new Gson().toJson("false"));
                }
                break;
            }
        case "/login":
            login = request.getParameter("login");
            password = request.getParameter("password");
            request.setAttribute("info", "Нет такого пользователя!");
            member = memberFacade.memberByLogin(login);
            if(member == null){
                request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                    .forward(request, response);
                break;
            }
            EncriptPass ep = new EncriptPass();
            String salts = member.getSalts();
            String encriptPass = ep.setEncriptPass(password, salts);
            if(encriptPass.equals(member.getPassword())){
                session = request.getSession(true);
                session.setAttribute("regUser", member);
                request.setAttribute("info", "Привет "+member.getUser().getName()
                        +"! Вы вошли в систему.");
                request.getRequestDispatcher(PageReturner.getPage("welcome"))
                        .forward(request, response);
                break;
            }
            request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                    .forward(request, response);
            break;
        case "/showLogin":
            request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                    .forward(request, response);
            break;
        case "/logout":
            if(session != null){
                session.invalidate();
                request.setAttribute("info", "Вы вышли из системы");
            }
            request.getRequestDispatcher(PageReturner.getPage("welcome"))
                    .forward(request, response);
            break;
        case "/showUserRoles":
            if(!sl.isRole(member, "ADMIN")){
                request.setAttribute("info", "У вас нет прав доступа к ресурсу");
                request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                        .forward(request, response);
                break;
            } 
            
            Map<Member,String> mapMembers = new HashMap<>();
            List<Member> listMembers = memberFacade.findAll();
            int n = listMembers.size();
            for(int i=0;i<n;i++){
                mapMembers.put(listMembers.get(i), sl.getRole(listMembers.get(i)));
            }
            List<Role> listRoles = roleFacade.findAll();
            request.setAttribute("mapMembers", mapMembers);
            request.setAttribute("listRoles", listRoles);
            request.getRequestDispatcher(PageReturner.getPage("showUserRoles"))
                    .forward(request, response);
            break;
        case "/changeUserRole":
            if(!sl.isRole(member, "ADMIN")){
                request.setAttribute("info", "У вас нет прав доступа к ресурсу");
                request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                    .forward(request, response);
                break;
            }
            String setButton = request.getParameter("setButton");
            String deleteButton = request.getParameter("deleteButton");
            String userId = request.getParameter("user");
            String roleId = request.getParameter("role");
            UserRoles ur=null;
            
            if(setButton != null && roleId != null){
                Role roleToUser = roleFacade.find(new Long(roleId));
                ur = new UserRoles(member, roleToUser);
                sl.addRoleToUser(ur);
            }
            if(deleteButton != null && roleId != null && userId != null){
                Role roleToUser = roleFacade.find(new Long(roleId));
                member = memberFacade.memberByUserId(new Long(userId));
                ur = new UserRoles(member, roleToUser);
                sl.deleteRoleToUser(ur.getMember());
            }
            if(userId == null && roleId == null && deleteButton != null){
                if((member = memberFacade.memberByLogin("login")) != null){
                    User user = member.getUser();
                    
                    List<UserRoles> listUserRoles = userRolesFacade.findByMember(member);
                    for(int i=0;i<listUserRoles.size();i++){
                        userRolesFacade.remove(listUserRoles.get(i));
                    }
                    memberFacade.remove(member);
                    userFacade.remove(user);
                    request.setAttribute("info", "тестовый пользователь удален");
                    request.getRequestDispatcher(PageReturner.getPage("welcome"))
                        .forward(request, response);
                    break;
                }
            }
            mapMembers = new HashMap<>();
            listMembers = memberFacade.findAll();   
            n = listMembers.size();
            for(int i=0;i<n;i++){
                mapMembers.put(listMembers.get(i), sl.getRole(listMembers.get(i)));
            }
            request.setAttribute("mapMembers", mapMembers);
            List<Role> newListRoles = roleFacade.findAll();
            request.setAttribute("listRoles", newListRoles);
            request.getRequestDispatcher(PageReturner.getPage("showUserRoles"))
                    .forward(request, response);
            break;
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
