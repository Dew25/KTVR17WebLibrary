/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import com.google.gson.Gson;
import entity.Book;
import entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import secure.SecureLogic;
import session.BookFacade;


/**
 *
 * @author jvm
 */
@Path("/books")
@Stateless
public class BookService {
    
    @Context private UriInfo uriInfo;
    @Context private HttpServletRequest request;
    SecureLogic secureLogic = new SecureLogic();
    @EJB private BookFacade bookFacade;
    public BookService() {
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String books(){
        List<Book> books = bookFacade.findActived(true);
        HttpSession session = request.getSession(false);
        if(session == null) return new Gson().toJson(new ArrayList<>());
        Member member = (Member) session.getAttribute("regUser");
        if(member == null ) return new Gson().toJson(new ArrayList<>());
        if(!books.isEmpty()){
            return new Gson().toJson(books);
        }else{
            return new Gson().toJson(new ArrayList<>());
        }
        
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String book(@PathParam("id") Long id){
        Book book = bookFacade.find(id);
        if(book != null){
            return new Gson().toJson(book);
        }else{
            return "false";
        }
    }
    @POST
    @Path("/createBook")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createBook(Book book){
        HttpSession session = request.getSession(false);
        if(session == null){
            return new Gson().toJson("false");
        }
        Member member = (Member) session.getAttribute("regUser");
        if(secureLogic.isRole(member, "ADMIN")){
            return new Gson().toJson("false");
        }
        bookFacade.create(book);
        return new Gson().toJson(book.getId());
    }
}
