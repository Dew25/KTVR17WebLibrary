/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restService;


import com.google.gson.Gson;
import entity.Book;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.BookFacade;

/**
 *
 * @author Melnikov
 */
@Path("/book")
@Stateless
public class BookService {
    @EJB private BookFacade bookFacade;

    public BookService() {
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String books(){
        List<Book> books = bookFacade.findActived(true);
        return new Gson().toJson(books);
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String book(@PathParam("id") Long id){
        Book book = bookFacade.find(id);
        return new Gson().toJson(book);
    }
}
