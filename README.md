# cybersecurity-project

This is a project created for the course [Cyber Security Base 2018](https://cybersecuritybase.mooc.fi/). 
The point is to create an application with at least five different flaws from the [OWASP top ten list](https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf).
Then there must be an essay of around 1000 words about the security flaws, and how to identify and fix them.

---

I created a (very) simple note app for the project. The app is built on top of the given template. The source for the app can be found from here: https://github.com/aapzu2/cybersecuritybase-project. 
The app allows the users to log in and write, list and show notes. Every route requires the user to be logged in.
The app has two users, 'ted' (password 'ted') and 'carol' (password 'carol'). After logging in there are three pages:
- /list      – On list page there is a list of the user's notes and a button to create a new one. If the user has no notes, the list is empty. Next to each note there is a button to the show page.
- /add       – On add page the user can add a new note. A note has a title and some content. After saving the note the user is taken to the list page.
- /show/{id} - On a show page a single note is shown.

The user should not be able to see other user's notes. However, there are some security flaws. 
The app has these security flaws from the Top 10 list:

### A1:2017-Injection

The app makes it possible to inject SQL code via the address bar.

Steps to identify:
   1. Log in as 'ted'
   1. Click 'Add note'
   1. Type in any title and any content
   1. Click 'Submit'
   1. On list page click 'Show' from the just created note
   1. Open another session as 'carol'
   1. Go to the show page (/show/{id})
   1. The page cannot be opened and you are redirected to the list page
   1. Go to the show page again but replace '{id}' with '{id}--' (the rest of the SQL clause is commented out) 
   1. The note opens

Steps to fix:
   - The SQL clause parameters should be escaped properly OR
   - An ORM should be used
   
### A3:2017 Sensitive Data Exposure

Steps to identify:
  - In the code (in `SecurityConfiguration.java`) we can see that a (very) bad encoder that does not encode anything is used. 
  Instead of encoding the password the 'encoder' returns the same password unchanged.
  
Steps to fix:
  - The password should be hashed with a one-way hashing function, for example with 'bcrypt'. Spring gives us `BCryptPasswordEncoder`, that could be used
   to properly hash the passwords before saving them to the database.
   
### A5:2017 Broken Access Control

The app does not have the functionality for removing a note, but by testing we can notice that it does actually has the route for it anyway.
By testing more we can notice that the route does not require any authentication.

Steps to identify:
  1. Log in as 'ted'
  1. Create a note
  1. On the list page click 'Show' from the note
  1. Check the id of the note from the url
  1. Log out
  1. Log in as 'carol'
  1. Open the dev tools to edit the html of the page
  1. Inject '<form action="/delete/{id}" method="POST"><button>Delete</button></form>' to the html where {id} is the id of the just created note.
  1. Click 'Delete'
  1. Log out
  1. Log back in as 'ted'
  1. You can see the note has been deleted
  
Steps to fix:
  - There should not be any controllers not used by any functionality (used by the basic user or any admin feature)
    - Any code can be a security threat so the amount of (extra) code should be minimized
  - A delete controller should check the owner of the resource (note in this case) before allowing the deletion
   
### A6:2017-Security Misconfiguration

The app has no proper error handling, and it shows the full exception stacktrace to the user. That information is something 
the user should never see and only the administrators of the app should have access to it.

Steps to identify:
  1. Log in as 'ted'
  1. Go to /show/4'RETURNING%20something-- (invalid sql, possible because of the  SQL injection flaw)
  1. Exception stacktrace opens
  
Steps to fix:
  - Proper null checks should be done and other vulnerable parts should be fixed (to minimize the probability of an internal error)
  - Expected errors (eg. because of invalid request payload) should be caught and handled as expected errors
  - In case an internal error actually happens, the app should only tell the user that an error has happened but not any information about it
  
### A7:2017-Cross-Site Scripting (XSS)

The html templates in the app have not been created properly and a XSS attack can be done. 
Now the user only has access to their own notes, but in any case where the user could see other users' notes that would be really dangerous. 

Steps to identify:
  1. Log in as 'ted'
  1. Click 'Add note'
  1. Type anything to the title
  1. Type "<script>alert('foo')</script>" to the content
  1. Click 'Submit'
  1. Note list page opens
  1. The script in the note content is executed.
  1. Alert is shown
  
Steps to fix:
  - All text shown in the html templates should be escaped 
    - Since the template is made with Thymeleaf an attribute th:text should be used instead of th:utext
    
### A10:2017-Insufficient Logging & Monitoring
    
The app has no logging at all (except the stack trace logging of thrown errors). 
There should be better logging, also for other purposes than exceptions and to other places than the console log.

Steps to fix:
  - Some kind of logging utility should be added
