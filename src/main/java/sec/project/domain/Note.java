package sec.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Entity
public class Note extends AbstractPersistable<String>  {

    private String title;
    private String content;

    @ManyToOne
    private Account account;

    public Note() {
        super();
    }

    public Note(String title, String content, Account account) {
        this();
        this.title = title;
        this.content = content;
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String address) {
        this.content = content;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
