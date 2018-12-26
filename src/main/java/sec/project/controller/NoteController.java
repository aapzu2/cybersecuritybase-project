package sec.project.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Note;
import sec.project.repository.AccountRepository;
import sec.project.repository.NoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;

@Controller
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManagerFactory emf;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Authentication authentication, @RequestParam String title, @RequestParam String content) {
        Account account = accountRepository.findByUsername(authentication.getName());

        Note note = new Note(title, content, account);
        noteRepository.save(note);
        return "redirect:/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam String id) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNativeQuery("DELETE NOTE WHERE ID = " + id);
        query.executeUpdate();
        return "redirect:/";
    }

    @RequestMapping(value = "/list")
    public String list(Authentication authentication, Model model) {
        Account account = accountRepository.findByUsername(authentication.getName());
        EntityManager em = emf.createEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM NOTE WHERE ACCOUNT_ID = " + account.getId(), Note.class);
        model.addAttribute("notes", query.getResultList());
        return "list";
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") String id, Authentication authentication, Model model) {
        Account account = accountRepository.findByUsername(authentication.getName());
        EntityManager em = emf.createEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM NOTE WHERE ID = '" + id + "' AND ACCOUNT_ID = " + account.getId(), Note.class);
        ArrayList<Note> notes = (ArrayList<Note>) query.getResultList();
        if (notes.size() > 0) {
            model.addAttribute("note", notes.get(0));
            return "show";
        }
        return "redirect:/";
    }

}
