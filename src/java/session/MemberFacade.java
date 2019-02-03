/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Member;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jvm
 */
@Stateless
public class MemberFacade extends AbstractFacade<Member> {

    @PersistenceContext(unitName = "KTVR17WebLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MemberFacade() {
        super(Member.class);
    }

    public Member memberByLogin(String login) {
        try {
            return (Member) em.createQuery("SELECT m FROM Member m WHERE m.login = :login")
                .setParameter("login", login)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Member memberByUserId(Long userId) {
        try {
            return (Member) em.createQuery("SELECT m FROM Member m WHERE m.user.id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
        
    }

   

    
    
}
