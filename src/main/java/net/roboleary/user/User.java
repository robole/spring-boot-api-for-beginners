package net.roboleary.user;

public class User {
    private long id;
    private String name;
    private int age;

    //you must include this when you have a POST or PUT, Spring needs to be able to create an empty object
    public User(){}

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //generated through IntelliJ. Equals and hashCode are important for object comparsion, so when we want to see
    //if we should delete an object from an array, we need to know if it the right object.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
