package net.roboleary.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private String basePath = "/users";
    private final User TEST_USER = new User(1, "Rob OLeary", 21);
    private final User TEST_USER_2 = new User(2, "Angela Merkel", 20);

    //no setUp method required because application context injects objects for us

    //Test Cases are the same as UserControllerTest, but we are using TestRestTemplate instead of MockMVC. I use
    //JSONAssert for my assertions, as an external test, it makes more sense to me to treat the body as a JSON string,
    // rather than converting it back to java objects. This is like taking a "consumer view".

    public void deleteUser(long id){
        restTemplate.delete(basePath + "/" + id);
    }

    public ResponseEntity<String> postUser(User user) throws Exception{
        return restTemplate.postForEntity(basePath, user, String.class);
    }

    //add 2 users
    public void postUsers() throws Exception{
       postUser(TEST_USER);
       postUser(TEST_USER_2);
    }

    @Test
    public void getUsers_2UsersExist_ReturnOK() throws Exception{
        //data prep
        postUsers();

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "[{id:1,name:\"Rob OLeary\",age:21},{id:2,name:\"Angela Merkel\",age:20}]";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
        deleteUser( 2);
    }

    @Test
    public void getUsers_NoUsers_ReturnOKEmptyArray() throws Exception{
        // no data prep required

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("[]",response.getBody(),JSONCompareMode.STRICT);
    }

    @Test
    public void getUsersById_NotExists_ReturnNotFound() throws Exception {
        // no data prep required

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "/1", String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();
    }

    @Test
    public void getUsersById_Exists_ReturnOK() throws Exception {
        // data prep
        postUser(TEST_USER);

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "/{id}", String.class, 1);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "{id:1,name:\"Rob OLeary\",age:21}";
        JSONAssert.assertEquals(expected, response.getBody(),JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void getUsersByName_Exists_ReturnOK() throws Exception{
        // data prep
        postUsers();

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "?name=rob oleary", String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "[{id:1,name:\"Rob OLeary\",age:21}]";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
        deleteUser( 2);
    }

    @Test
    public void getUsersByName_NoUser_ReturnNotFound() throws Exception{
        // data prep
        postUser(TEST_USER);

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "?name=x", String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void getUsersByAge_Exists_ReturnOK() throws Exception{
        // data prep
        postUsers();

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "?age=21", String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "[{id:1,name:\"Rob OLeary\",age:21}]";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
        deleteUser( 2);
    }

    @Test
    public void getUsersByAge_NoUser_ReturnNotFound() throws Exception{
        // no data prep

        // execute
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + "?age=1", String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();
    }

    @Test
    public void add_NewUser_Success() throws Exception {
        // execute
        ResponseEntity<String> response = postUser(TEST_USER);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String expected = "{id:1,name:\"Rob OLeary\",age:21}";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void addOrUpdate_NewUser_Created() throws Exception {
        // execute
        HttpEntity<String> request = new HttpEntity(TEST_USER);
        ResponseEntity<String> response = restTemplate.exchange(basePath, HttpMethod.PUT, request, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String expected = "{id:1,name:\"Rob OLeary\",age:21}";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void addOrUpdate_ExistingUserSameData_OK() throws Exception {
        // data prep
        postUser(TEST_USER);

        // execute
        HttpEntity<String> request = new HttpEntity(TEST_USER);
        ResponseEntity<String> response = restTemplate.exchange(basePath, HttpMethod.PUT, request, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "{id:1,name:\"Rob OLeary\",age:21}";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void addOrUpdate_ExistingUserNewData_OK() throws Exception {
        // data prep
        postUser(TEST_USER);
        User updateUser = new User(1,"John Doe",20);

        // execute
        HttpEntity<String> request = new HttpEntity(updateUser);
        ResponseEntity<String> response = restTemplate.exchange(basePath, HttpMethod.PUT, request, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "{id:1,name:\"John Doe\",age:20}";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.STRICT);

        //cleanup
        deleteUser( 1);
    }

    @Test
    public void delete_NotExists_ReturnNotFound() throws Exception {
        // no data prep required

        // execute
        HttpEntity<String> request = new HttpEntity(null);
        ResponseEntity<String> response = restTemplate.exchange(basePath + "/1", HttpMethod.DELETE, request, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_Exists_OK() throws Exception {
        // no data prep required
        postUser(TEST_USER);

        // execute
        HttpEntity<String> request = new HttpEntity(null);
        ResponseEntity<String> response = restTemplate.exchange(basePath + "/1", HttpMethod.DELETE, request, String.class);

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}