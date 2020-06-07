# Build an API with Spring Boot

This is the code for [this tutorial](https://roboleary.net/java/2020/05/27/spring-boot-api.html). You can
read that for a complete rundown of the code.

**Tests for this code are available in the branch `with-tests`.** I have kept it separate
to be consistent with the tutorial.
  
## What you’ll build
 
 You’ll build a web service that can perform the actions specified in the table below.
 
 The default local address for your Spring Boot application will be: [http://localhost:8080](http://localhost:8080),
 so the address to get all users would be [http://localhost:8080/users](http://localhost:8080/users) for example.
 
 <table class="tg">
   <tbody><tr>
     <th class="tg-yw4l">HTTP Method</th>
     <th class="tg-yw4l">Address</th>
     <th class="tg-yw4l">Action</th>
   </tr>
   <tr>
     <td class="tg-yw4l">GET</td>
     <td class="tg-yw4l">/users</td>
     <td class="tg-yw4l">Get all users</td>
   </tr>
   <tr>
     <td class="tg-yw4l">GET</td>
     <td class="tg-yw4l">/users/{id}</td>
     <td class="tg-yw4l">Get users by id</td>
   </tr>
   <tr>
     <td class="tg-yw4l">GET</td>
     <td class="tg-yw4l">/users?name=rob+oleary</td>
     <td class="tg-yw4l">Get user by name</td>
   </tr>
   <tr>
     <td class="tg-yw4l">POST</td>
     <td class="tg-yw4l">/users</td>
     <td class="tg-yw4l">Add a new user</td>
   </tr>
   <tr>
     <td class="tg-yw4l">PUT</td>
     <td class="tg-yw4l">/users</td>
     <td class="tg-yw4l">Update a user</td>
   </tr>
   <tr>
     <td class="tg-yw4l">DELETE</td>
     <td class="tg-yw4l">/users/{id}</td>
     <td class="tg-yw4l">Delete a user</td>
   </tr>
 </tbody>
 </table>