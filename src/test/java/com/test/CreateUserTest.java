package com.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;



import static io.restassured.RestAssured.given;

public class CreateUserTest
{
    String baseUrl = "https://mzo5slmo45.execute-api.eu-west-2.amazonaws.com";
    String apiKey = "GombImxOhMCa8AqMmNM9KEFwaSHSFHty";
    String inValidapiKey = "GomhMCa8AqmNM9KEFwaSHSFHty";

    Util util = new Util();

    /**
     * Test to verify the create user api Happy path
     */

    @Test(description = "To Create a new user", priority=1)
    public void createUser(){
        String ratingResponse="";

            JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");
        userObj.put("firstName" , util.createRandomChar(10));
        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1987-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);
        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        Assert.assertTrue(response.statusCode()==200);
        Assert.assertTrue(response.path("status").equals("Success"));



        ratingResponse= util.validateRating(Integer.parseInt(userObj.get("rating").toString()));
        Assert.assertTrue(response.path("data.status").equals(ratingResponse));
    }
    /**
     * Test to verify the get user api Happy path
     */
    @Test(description = "To get a user", priority=1)
    public void getUser(){
        JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");
        userObj.put("firstName" , util.createRandomChar(10));
        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1984-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);

        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        String userId = response.path("data.userId");
        Response getResponse =
                given().header("Authorization", apiKey)
                        .when().get(baseUrl+"/v1/users/"+userId)
                        .then().extract().response();
        Assert.assertTrue(getResponse.path("data.rating").toString().equals(userObj.get("rating").toString()));
        Assert.assertTrue(getResponse.path("data.lastName").equals(userObj.get("lastName").toString()));
        Assert.assertTrue(getResponse.path("data.firstName").equals(userObj.get("firstName").toString()));
        Assert.assertTrue(getResponse.path("data.dateOfBirth").equals(userObj.get("dateOfBirth").toString()));
        Assert.assertTrue(getResponse.path("data.email").equals(userObj.get("email").toString()));
        Assert.assertTrue(getResponse.path("data.title").equals(userObj.get("title").toString()));
        Assert.assertTrue(getResponse.path("data.status").toString().equals(util.validateRating(Integer.parseInt(getResponse.path("data.rating").toString()))));
    }

    /**
     * Test to verify the create user api without mandatory field lastName
     */
    @Test(description = "Unhappy path Create user", priority=1)
    public void createUserUnHappyPath_NoLastName(){
        JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");
        userObj.put("firstName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1984-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);

        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        Assert.assertTrue(response.statusCode()==400);
         Assert.assertTrue(response.path("errorType").equals("BadRequest"));
        Assert.assertTrue(response.path("errorMessage").equals("Validation error - last name must be between 2 and 255 characters"));
    }

    /**
     * Test to verify the create user api without mandatory field FirstName
     */

    @Test(description = "Unhappy path Create user", priority=1)
    public void createUserUnHappyPath_NoFirstName(){
        JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");

        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1984-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);

        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        Assert.assertTrue(response.statusCode()==400);
        Assert.assertTrue(response.path("errorType").equals("BadRequest"));
        Assert.assertTrue(response.path("errorMessage").toString().equals("Validation error - first name must be between 2 and 255 characters"));
    }

    /**
     * Test to verify the request create user api without api key
     */

    @Test(description = "Unhappy path Create user UnAuthorized", priority=1)
    public void creatUserUnAuthorized(){
        JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");
        userObj.put("firstName" , util.createRandomChar(10));

        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1984-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);

        Response response =  given().
                contentType(ContentType.JSON)
                .body(userObj.toString())
                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        Assert.assertTrue(response.statusCode()==401);
        Assert.assertTrue(response.path("message").equals("Unauthorized"));
    }

    /**
     * Test to verify the request get user api without api key
     */
    @Test(description = "Unhappy path get user UnAuthorized", priority=1)
    public void getUserUnAuthorized(){
        JSONObject userObj = new JSONObject();

        userObj.put("title" , "Mr");
        userObj.put("firstName" , util.createRandomChar(10));
        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1984-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);

        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        String userId = response.path("data.userId");
        Response getResponse =
                given()
                        .when().get(baseUrl+"/v1/users/"+userId)
                        .then().extract().response();
        System.out.println(response.statusCode());
        Assert.assertTrue(getResponse.statusCode()==401);
        Assert.assertTrue(getResponse.path("message").equals("Unauthorized"));
    }

    /**
     * Test to verify the request with only Mandatory fields
     */

    @Test(description = "To Create a new user", priority=1)
    public void createUser_OnlyMandatoryFields(){
        String ratingResponse="";

        JSONObject userObj = new JSONObject();

        userObj.put("firstName" , util.createRandomChar(10));
        userObj.put("lastName" , util.createRandomChar(10));
        userObj.put("dateOfBirth" , "1987-06-04");
        userObj.put("email" , util.createRandomChar(7)+"@gmail.com");
        userObj.put("password" , "super secret password");
        userObj.put("rating" , 10);
        Response response =  given().
                contentType(ContentType.JSON)
                .header("Authorization", apiKey)
                .body(userObj.toString())

                .when().post(baseUrl+"/v1/users")
                .then().extract().response();
        System.out.println(response.statusCode());
        Assert.assertTrue(response.statusCode()==200);
        Assert.assertTrue(response.path("status").equals("Success"));
    }


}

