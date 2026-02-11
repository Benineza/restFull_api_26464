Question 1 — Library Book Management API



How to Run the application



1\. Open the project in IntelliJ / VS Code

2\. Run `Question1LibraryApiApplication.java`

3\. Server starts on: `http://localhost:8080`



---



API Endpoints



1\. Get All Books



type in browser: http://localhost:8080/api/books/



output:



\[{"id":1,"title":"JAVA","author":"Manzi Moise","isbn":"978-0132350884","publicationYear":2020},{"id":2,"title":"HTML \& CSS","author":"Cyusa Kevin","isbn":"978-0134685991","publicationYear":2021},{"id":3,"title":"Spring Boot","author":"Sam David","isbn":"978-1617294945","publicationYear":2022}]



2\. Get Book by ID



type in browser: http://localhost:8080/api/books/1



Output:



{"id":1,"title":"JAVA","author":"Manzi Moise","isbn":"978-0132350884","publicationYear":2020}



3\. Search Book by Title



type in browser: http://localhost:8080/api/books/search?title=JAVA



output:



\[{"id":1,"title":"JAVA","author":"Manzi Moise","isbn":"978-0132350884","publicationYear":2020}]



4\. Add New Book



POST /api/books



Body:



{

&nbsp; "title": "Java Basics",

&nbsp; "author": "Moise",

&nbsp; "isbn": "123456789",

&nbsp; "publicationYear": 2026

}



5\. Delete Book



DELETE /api/books/{id}



HTTP Status Codes Used



Testing



APIs tested using Browser and Postman successfully.



