/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import com.google.gson.Gson;
import entity.Member;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import secure.SecureLogic;
import session.MemberFacade;
import util.EncriptPass;

/**
 *
 * @author jvm
 */
@Path("secure")
@Stateless
public class LoginService {
    @EJB private MemberFacade memberFacade;
    @Context private UriInfo uriInfo;
    private SecureLogic secureLogic = new SecureLogic();
    private EncriptPass ep = new EncriptPass();
    @Context private HttpServletRequest request;
    
    public LoginService() {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(Credential credential){
        String login = credential.getLogin();
        String password = credential.getPassword();
        
        if(login == null) return new Gson().toJson(false);
        
        Member member = memberFacade.memberByLogin(login);
        if(member == null) return new Gson().toJson(false);
        if(member.getPassword().equals(ep.setEncriptPass(password, member.getSalts()))){
            HttpSession session = request.getSession(true);
            session.setAttribute("regUser", member);
            return new Gson().toJson(member.getUser());
        }
        return new Gson().toJson(false);
    }
    
}
