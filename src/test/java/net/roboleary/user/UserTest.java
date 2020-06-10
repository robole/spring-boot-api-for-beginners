package net.roboleary.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class UserTest {

    User testUser1 = null;

    @BeforeEach
    void setUp() {
        testUser1 = createNewUser();
    }

    @AfterEach
    void tearDown() {
        testUser1 = null;
    }

    User createNewUser(){
        return new User(1,"Rob OLeary", 33);
    }

    //naming convention I follow is: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void getId_1_ReturnValue(){
        Assertions.assertEquals(1, testUser1.getId());
    }

    @Test
    void setId_2_ChangeValue(){
        testUser1.setId(2);
        Assertions.assertEquals(2, testUser1.getId());
    }

    @Test
    void getName_RobOLeary_ReturnValue(){
        Assertions.assertEquals("Rob OLeary", testUser1.getName());
    }

    @Test
    void setId_JohnDoe_ChangeValue(){
        testUser1.setName("John Doe");
        Assertions.assertEquals("John Doe", testUser1.getName());
    }

    @Test
    void getAge_33_ReturnValue(){
        Assertions.assertEquals(33, testUser1.getAge());
    }

    @Test
    void setAge_JohnDoe_ChangeValue(){
        testUser1.setName("John Doe");
        Assertions.assertEquals("John Doe", testUser1.getName());
    }

    @Test
    void equals_SameId_True(){
        User testUser2 = new User(1, "Duplicate", 1);
        Assertions.assertEquals(true, testUser1.equals(testUser2));
    }

    @Test
    void equals_DifferentId_True(){
        User testUser2 = new User(2, "Different", 1);
        Assertions.assertEquals(false, testUser1.equals(testUser2));
    }

    @Test
    void hashCode_UniqueCode_ReturnCorrectObject(){
        User testUser2 = new User(2, "Different", 1);
        ArrayList<User> users = new ArrayList<User>();

        users.add(testUser1);
        users.add(testUser2);

        Assertions.assertEquals(true,users.contains(testUser1));
        Assertions.assertEquals(true,users.contains(testUser2));
    }

}
