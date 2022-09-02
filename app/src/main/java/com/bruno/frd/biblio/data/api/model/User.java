package  com.bruno.frd.biblio.data.api.model;

/**
 * Objeto plano Java para representar usuarios
 */
public class User {

    private String id;
    private String name;
    private String last_name;
    private String file;
    private String token;
    private String address;
    private String city;
    private String phone;
    private String dni;
    private String mail;

    public User(String id, String name, String last_name, String file, String token, String address, String city, String phone, String dni, String mail) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.file = file;
        this.token = token;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.dni = dni;
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String address) {
        this.last_name = last_name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String gender) {
        this.file = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
