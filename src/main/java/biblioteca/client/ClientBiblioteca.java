package biblioteca.client;

import java.net.ConnectException;
import java.util.Scanner;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ClientBiblioteca {
    public static void main(String[] args) {
        // Collegamento al servizio "biblioteca"
        Client c = ClientBuilder.newClient();
        WebTarget biblio = c.target("http://localhost:50004/biblioteca");
        
        // Prepara interazione con utente
        Scanner sc = new Scanner(System.in);
        String scelta = "";
        
        // Interazione con utente
        while(!scelta.equalsIgnoreCase("exit")) {
            System.out.println();
            System.out.print("Digita l'operazione da effettuare: ");
            scelta = sc.nextLine();
            
            // POST
            if(scelta.equalsIgnoreCase("post")) post(biblio);
            else if(scelta.equalsIgnoreCase("get")) get(biblio);
            else if(scelta.equalsIgnoreCase("put")) put(biblio);
            else if(scelta.equalsIgnoreCase("delete")) delete(biblio);
            else if(scelta.equalsIgnoreCase("exit")) {
                System.out.println("Ciao!");
            }
            else {
                System.out.println("Operazione non consentita. Digitare una tra POST, GET, PUT, DELETE, o EXIT");
            }
        }
    }

    private static void post(WebTarget biblio) {
        Scanner sc = new Scanner(System.in);
        JSONObject libro = new JSONObject();
        // Lettura isbn
        System.out.print("Digita ISBN: ");
        libro.put("isbn", sc.nextLine());
        // Lettura titolo
        System.out.print("Digita titolo: ");
        libro.put("titolo", sc.nextLine());
        // Lettura autori
        JSONArray autori = new JSONArray();
        System.out.print("Digita primo autore: ");
        String autore = sc.nextLine();
        while(autore.length() > 0) {
            autori.add(autore);
            System.out.print("Digita altro autore (invio per terminare): ");
            autore = sc.nextLine();
        }
        libro.put("autori",autori);
        // Lettura editore
        System.out.print("Digita editore: ");
        libro.put("editore",sc.nextLine());
        // Lettura descrizione
        System.out.print("Digita descrizione: ");
        libro.put("descrizione", sc.nextLine());
        
        // Esecuzione della POST
        Response r;
        try {
            r = biblio.request().post(Entity.entity(
                    libro.toJSONString(),
                    MediaType.APPLICATION_JSON
            ));
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.err.println("ERRORE: Impossibile connettersi al servizio");
                return;
            } else throw e;
        }
        // Se la risorsa è stata creata
        if(r.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("Libro aggiunto: " + r.getHeaders().get("location"));
        }
        else {
            System.err.println("ERRORE: POST non riuscita (" + r.getStatusInfo() + ")");
        }
    }

    private static void get(WebTarget biblio) {
        Scanner sc = new Scanner(System.in);
        // Lettura isbn
        System.out.print("Digita ISBN: ");
        String isbn = sc.nextLine();
        // Richiesta GET
        Response r;
        try {
            r = biblio.path(isbn).request().get();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.err.println("ERRORE: Impossibile connettersi al servizio");
                return;
            } else throw e;
        }
        // Se ha trovato il libro, ne stampa il contenuto
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(r.readEntity(String.class));
        }
        else { 
            System.err.println("ERRORE: Impossibile recuperare il libro richiesto (" + r.getStatusInfo() + ")");
        }
    }

    private static void put(WebTarget biblio) {
        Scanner sc = new Scanner(System.in);
        JSONObject libro = new JSONObject();
        // Lettura isbn
        System.out.print("Digita ISBN: ");
        libro.put("isbn", sc.nextLine());
        // Lettura titolo
        System.out.print("Digita titolo: ");
        libro.put("titolo", sc.nextLine());
        // Lettura autori
        JSONArray autori = new JSONArray();
        System.out.print("Digita primo autore: ");
        String autore = sc.nextLine();
        while(autore.length() > 0) {
            autori.add(autore);
            System.out.print("Digita altro autore (invio per terminare): ");
            autore = sc.nextLine();
        }
        libro.put("autori",autori);
        // Lettura editore
        System.out.print("Digita editore: ");
        libro.put("editore",sc.nextLine());
        // Lettura descrizione
        System.out.print("Digita descrizione: ");
        libro.put("descrizione", sc.nextLine());
        
        // Richiesta PUT
        Response r;
        try {
            String isbn = (String) libro.get("isbn");
            r = biblio.path(isbn).request().put(Entity.entity(
                    libro.toJSONString(),
                    MediaType.APPLICATION_JSON
            ));
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.err.println("ERRORE: Impossibile connettersi al servizio");
                return;
            } else throw e;
        }
        // Se la risorsa è stata creata
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Libro aggiunto: " + r.getHeaders().get("location"));
        }
        else {
            System.err.println("ERRORE: PUT non riuscita (" + r.getStatusInfo() + ")");
        }
    }

    private static void delete(WebTarget biblio) {
        Scanner sc = new Scanner(System.in);
        // Lettura isbn
        System.out.print("Digita ISBN: ");
        String isbn = sc.nextLine();
        // Richiesta DELETE
        Response r;
        try {
            r = biblio.path(isbn).request().delete();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.err.println("ERRORE: Impossibile connettersi al servizio");
                return;
            } else throw e;
        }
        // Se ha trovato il libro, ne stampa il contenuto
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Libro eliminato.");
        }
        else { 
            System.err.println("ERRORE: Impossibile eliminare il libro richiesto (" + r.getStatusInfo() + ")");
        }
    }
}
