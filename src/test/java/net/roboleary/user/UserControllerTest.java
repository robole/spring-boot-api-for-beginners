package net.roboleary.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how to test a controller using MockMVC with Standalone setup. No server is run, and
 * no Spring context is created. This is the closest you can get to a "unit test" for a controller.
 *
 * @author rob oleary
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mvc;

    @InjectMocks
    private UserController userController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<User> jsonUser;

    private String basePath = "/users";

    private final User TEST_USER = new User(1, "Rob OLeary", 21);
    private final User TEST_USER_2 = new User(2, "Angela Merkel", 20);

    @BeforeEach
    public void setup() {
        // Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    public void addUser() throws Exception{
        // add first user
        mvc.perform(
                post(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER).getJson()
                )).andReturn().getResponse();
    }

    //add 2 users
    public void addUsers() throws Exception{
        mvc.perform(
                post(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER).getJson()
                )).andReturn().getResponse();

        mvc.perform(
                post(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER_2).getJson()
                )).andReturn().getResponse();
    }

    @Test
    public void getUsers_2UsersExist_ReturnOK() throws Exception{
        //data prep
        addUsers();

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonUser1 = jsonUser.write(TEST_USER).getJson();
        String jsonUser2=  jsonUser.write(TEST_USER_2).getJson();
        String jsonUserArray = "[" + jsonUser1 + "," + jsonUser2 + "]";

        assertThat(response.getContentAsString()).isEqualTo(jsonUserArray);
    }

    @Test
    public void getUsers_NoUsers_ReturnOKEmptyArray() throws Exception{
        // no data prep required

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getUsersById_NotExists_ReturnNotFound() throws Exception {
        // no data prep required

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void getUsersById_Exists_ReturnOK() throws Exception {
        // data prep
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUser.write(TEST_USER).getJson()
        );
    }

    @Test
    public void getUsersByName_Exists_ReturnOK() throws Exception{
        // data prep
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "?name=rob oleary").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                "[" + jsonUser.write(TEST_USER).getJson() + "]"
        );
    }

    @Test
    public void getUsersByName_NoUser_ReturnNotFound() throws Exception{
        // data prep
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "?name=x").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void getUsersByAge_Exists_ReturnOK() throws Exception{
        // data prep
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "?age=21").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
               "[" + jsonUser.write(TEST_USER).getJson() + "]"
        );
    }

    @Test
    public void getUsersByAge_NoUser_ReturnNotFound() throws Exception{
        // no data prep

        // execute
        MockHttpServletResponse response = mvc.perform(
                get(basePath + "?age=21").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void add_NewUser_Success() throws Exception {
        // execute
        MockHttpServletResponse response = mvc.perform(
                post(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER).getJson()
                )).andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUser.write(TEST_USER).getJson()
        );
    }

    @Test
    public void addOrUpdate_NewUser_Created() throws Exception {
        // execute
        MockHttpServletResponse response = mvc.perform(
                put(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER).getJson()
                )).andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUser.write(TEST_USER).getJson()
        );
    }

    @Test
    public void addOrUpdate_ExistingUserSameData_OK() throws Exception {
        // data prep
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                put(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(TEST_USER).getJson()
                )).andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUser.write(TEST_USER).getJson()
        );
    }

    @Test
    public void addOrUpdate_ExistingUserNewData_OK() throws Exception {
        // data prep
        addUser();
        User updateUser = new User(1,"John Doe",20);

        // execute
        MockHttpServletResponse response = mvc.perform(
                put(basePath).contentType(MediaType.APPLICATION_JSON).content(
                        jsonUser.write(updateUser).getJson()
                )).andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUser.write(updateUser).getJson()
        );
    }

    @Test
    public void delete_NotExists_ReturnNotFound() throws Exception {
        // no data prep required

        // execute
        MockHttpServletResponse response = mvc.perform(
                delete(basePath + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void delete_Exists_OK() throws Exception {
        // no data prep required
        addUser();

        // execute
        MockHttpServletResponse response = mvc.perform(
                delete(basePath + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
