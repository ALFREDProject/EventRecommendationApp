package alfred.eu.eventrecommendationapp.web;

/**
 * Created by thardes on 25/04/2016.
 */
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.util.List;


public class WebServiceClient {

    public List<Object> doGetRequest(String urlToCall, Class entityClass) {
        List<Object> resultList = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToCall);

            // GET method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").get(ClientResponse.class);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            String resultOfRequest = response.getEntity(String.class);
            //System.out.println(resultOfRequest);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            resultList = mapper.readValue(resultOfRequest,TypeFactory.defaultInstance().constructCollectionType(List.class, entityClass));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }/*

    public Boolean doGetRequest(String urlToCall) {
        Boolean result = false;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToCall);
            // GET method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").get(ClientResponse.class);
            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String tempResult = response.getEntity(String.class);
            if(tempResult.equals("true"))
                result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String doPostRequestToCreate(String urlToPost, String jsonToPost) {
        String resultOfRequest = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToPost);

            // POST method
            ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").type("application/json").post(ClientResponse.class, jsonToPost);
            //ClientResponse response = webResource.accept("application/raw").type("application/x-www-form-urlencoded").type("application/json").post(ClientResponse.class, jsonToPost);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);//This returns always "null"

            System.out.println("Created resource .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultOfRequest;
    }

    public List<Object> doPostRequestToRetrieve(String urlToPost, String jsonToPost, Class entityClass) {
        List<Object> resultList = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToPost);

            // POST method
            ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").type("application/json").post(ClientResponse.class, jsonToPost);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            String resultOfRequest = response.getEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            resultList = mapper.readValue(resultOfRequest,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, entityClass));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public String doPutRequest(String urlToPut, String jsonToPut) {
        String resultOfRequest = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToPut);

            // PUT method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").type("application/json").put(ClientResponse.class, jsonToPut);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);
            System.out.println("Updated resource .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultOfRequest;
    }

    public String doDeleteRequest(String urlToDelete) {
        String resultOfRequest = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToDelete);

            // DELETE method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").delete(ClientResponse.class);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);
            System.out.println("Deleted resource .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfRequest;
    }


    public Object doDeleteRequestEM(String urlToDelete, Class entityClass) {
        String resultOfRequest = null;
        Object result = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(urlToDelete);

            // DELETE method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").delete(ClientResponse.class);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);


            // display response
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(resultOfRequest,entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/
}