# cybersecurity-project

This is a project created for the course [Cyber Security Base 2018](https://cybersecuritybase.mooc.fi/). 
The point is to create an application with at least five different flaws from the [OWASP top ten list](https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf).
Then there must be an essay of around 1000 words about the security flaws, and how to identify and fix them.

---

I created a (very) simple note app for the project. The source for the app can be found from here: https://github.com/aapzu2/cybersecuritybase-project. The app allows the users to log in and write, list and show notes.
The app has two users, 'ted' (password 'ted') and 'carol' (password 'carol'). After logging in there are three pages:
- /list      – lists the notes of the user
- /add       – add a new note
- /show/{id} - show a specific note

The user should not be able to see other user's notes. However, there are some security flaws. 
The app has these security flaws from the Top 10 list:

### A1:2017-Injection

To reproduce:
   1. Log in as 'ted'
   2. Create a note
   3. Click 'Show'
   4. Open another session as 'carol'
   5. Go to the show page (/show/{id})
   6. The page cannot be opened
   7. Replace '{id}' with '{id}--' (the rest of the SQL clause is commented out) 
   8. The note opens

To fix:
   - The SQL clause parameters should be escaped properly OR
   - An ORM should be used
   
### A3:2017 Sensitive Data Exposure

To identify:
  - In the code (in `SecurityConfiguration.java`) we can see that a (very) bad encoder that does not encode anything is used
  
To fix:
  - Use for example `BCryptPasswordEncoder` to properly encode the passwords before saving them to the database.
   
### A6:2017-Security Misconfiguration

The app has no proper error handling, and it shows the full exception stacktrace to the user.

To reproduce:
  1. Log in as 'ted'
  2. Go to /show/4'RETURNING%20something-- (invalid sql, possible because of the  SQL injection flaw)
  3. Exception stacktrace opens
  
To Fix:
  - Proper null checks and other vulnerable parts should be fixed (to minimize the probability of an internal error)
  - In case an internal error actually happens, the app should only tell the user that an error has happened but not any information about it
  
### A7:2017-Cross-Site Scripting (XSS)

To reproduce:
  1. Log in as 'ted'
  2. Click 'Add note'
  3. Type anything to the title
  4. Type "<script>alert('foo')</script>" to the content
  5. Click 'Submit'
  6. Note list opens and the script in the note content is executed.
  7. Alert is shown
  
To fix:
  - Escape all text show in the html templates
    - Use th:text instead of th:utext
    
### A10:2017-Insufficient Logging & Monitoring
    
The app has no logging at all (except the stack trace logging of thrown errors). 
There should be better logging, also for other purposes than exceptions and to other places than the console log.

To fix:
  - Some kind of logging utility should be added
